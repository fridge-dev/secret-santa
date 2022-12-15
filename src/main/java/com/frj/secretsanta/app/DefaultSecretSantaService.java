package com.frj.secretsanta.app;

import com.frj.secretsanta.app.api.ClientException;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.PersonData;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.internal.assignment.ImmutableAssignmentInput;
import com.frj.secretsanta.app.internal.assignment.SecretSantaAssigner;
import com.frj.secretsanta.app.internal.format.SantaMessageFormatter;
import com.frj.secretsanta.app.internal.sms.ImmutableSmsInput;
import com.frj.secretsanta.app.internal.sms.SmsMessenger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DefaultSecretSantaService implements SecretSantaService {

    private final SecretSantaAssigner assigner;

    private final SmsMessenger messenger;

    public DefaultSecretSantaService(SecretSantaAssigner assigner, SmsMessenger messenger) {
        this.assigner = Objects.requireNonNull(assigner);
        this.messenger = Objects.requireNonNull(messenger);
    }

    @Override
    public SecretSantaBroadcastOutput broadcastMessage(final SecretSantaBroadcastInput input) throws ClientException {
        validateInput(input);

        final SecretSantaAssigner.AssignmentOutput assignmentOutput = getAssignments(input);

        final List<DecoratedSmsInput> smsInputs = createSmsInputs(input, assignmentOutput.assignments());
        final Set<String> failedPersonIds = broadcastSms(smsInputs);

        return ImmutableSecretSantaBroadcastOutput.builder()
                .failedPersonIds(failedPersonIds)
                .build();
    }

    private void validateInput(final SecretSantaBroadcastInput input) throws ClientException {
        if (null == input) {
            throw new ClientException("Input is required");
        }
        if (input.messageFormat().isBlank()) {
            throw new ClientException("Message is required");
        }
        if (input.personIdsToMessage().isEmpty()) {
            throw new ClientException("PersonIdsToMessage is required");
        }
        if (input.people().isEmpty()) {
            throw new ClientException("People are required");
        }
        if (input.rngSeed() == 0) {
            throw new ClientException("RngSeed is required");
        }

        // Validate unique personIds and phoneNumbers
        if (input.allPersonIds().size() != new HashSet<>(input.allPersonIds()).size()) {
            throw new ClientException("Duplicate person in people list");
        }
        if (input.phoneNumberByPersonId().values().size() != new HashSet<>(input.phoneNumberByPersonId().values()).size()) {
            throw new ClientException("Duplicate phone number in people list");
        }

        // Validate personIdsToMessage is subset of people
        for (String personIdToMessage : input.personIdsToMessage()) {
            if (!input.allPersonIds().contains(personIdToMessage)) {
                throw new ClientException(String.format("PersonIdToMessage '%s' is missing in people list", personIdToMessage));
            }
        }
    }

    private SecretSantaAssigner.AssignmentOutput getAssignments(final SecretSantaBroadcastInput apiInput) {
        ImmutableAssignmentInput assignmentInput = ImmutableAssignmentInput.builder()
                .personIds(apiInput.allPersonIds())
                .exclusions(apiInput.exclusions())
                .rngSeed(apiInput.rngSeed())
                .build();

        return assigner.getAssignments(assignmentInput);
    }

    // TODO use some personDatabase object here instead of service input.
    private List<DecoratedSmsInput> createSmsInputs(
            final SecretSantaBroadcastInput serviceInput,
            final List<SecretSantaAssigner.Assignment> assignments
    ) throws ClientException {
        final SantaMessageFormatter santaMessageFormatter = SantaMessageFormatter.create(serviceInput.messageFormat());

        List<DecoratedSmsInput> smsInputs = new ArrayList<>();

        for (SecretSantaAssigner.Assignment assignment : assignments) {
            final PersonData giftGiver = serviceInput.peopleByPersonId().get(assignment.giverPersonId());
            final PersonData giftReceiver = serviceInput.peopleByPersonId().get(assignment.receiverPersonId());
            if (!serviceInput.personIdsToMessage().contains(giftGiver.personId())) {
                continue;
            }

            final String phoneNumber = giftGiver.phoneNumber();
            final String messagePayload = santaMessageFormatter.format(giftGiver, giftReceiver);

            final SmsMessenger.SmsInput smsInput = ImmutableSmsInput.builder()
                    .phoneNumber(phoneNumber)
                    .messagePayload(messagePayload)
                    .build();

            smsInputs.add(new DecoratedSmsInput(smsInput, giftGiver.personId(), giftReceiver.personId()));
        }

        return smsInputs;
    }

    private Set<String> broadcastSms(final List<DecoratedSmsInput> inputs) {
        final Set<String> failedPersonIds = new HashSet<>();
        for (DecoratedSmsInput input : inputs) {
            final boolean success = messenger.sendSms(input.smsInput);
            if (!success) {
                failedPersonIds.add(input.recipientPersonId);
            }
        }

        return failedPersonIds;
    }

    private static final class DecoratedSmsInput {
        final SmsMessenger.SmsInput smsInput;
        final String recipientPersonId;
        final String targetPersonId;

        private DecoratedSmsInput(SmsMessenger.SmsInput input, String recipientPersonId, String targetPersonId) {
            this.smsInput = Objects.requireNonNull(input);
            this.recipientPersonId = Objects.requireNonNull(recipientPersonId);
            this.targetPersonId = Objects.requireNonNull(targetPersonId);
        }
    }
}
