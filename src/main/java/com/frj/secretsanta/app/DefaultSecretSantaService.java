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
        // Do this before any logic, because it also validates input.
        final SantaMessageFormatter santaMessageFormatter = createSantaMessageFormatter(input);

        final SecretSantaAssigner.AssignmentOutput assignmentOutput = getAssignments(input);

        final Set<String> failedPersonIds = broadcastSms(input, assignmentOutput.assignments(), santaMessageFormatter);

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

    private SantaMessageFormatter createSantaMessageFormatter(final SecretSantaBroadcastInput input) throws ClientException {
        final SantaMessageFormatter messageFormatter = SantaMessageFormatter.create(input.messageFormat());
        for (PersonData person : input.people()) {
            messageFormatter.performPreValidation(person);
        }

        return messageFormatter;
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
    private Set<String> broadcastSms(
            final SecretSantaBroadcastInput serviceInput,
            final List<SecretSantaAssigner.Assignment> assignments,
            final SantaMessageFormatter santaMessageFormatter
    ) {
        final Set<String> failedPersonIds = new HashSet<>();
        for (SecretSantaAssigner.Assignment assignment : assignments) {
            final PersonData giftGiver = serviceInput.peopleByPersonId().get(assignment.giverPersonId());
            final PersonData giftReceiver = serviceInput.peopleByPersonId().get(assignment.receiverPersonId());
            if (!serviceInput.personIdsToMessage().contains(giftGiver.personId())) {
                continue;
            }

            final boolean success = sendSms(giftGiver, giftReceiver, santaMessageFormatter);

            if (!success) {
                failedPersonIds.add(assignment.giverPersonId());
            }
        }
        return failedPersonIds;
    }

    private boolean sendSms(final PersonData giftGiver, final PersonData giftReceiver, final SantaMessageFormatter santaMessageFormatter) {
        final String phoneNumber = giftGiver.phoneNumber();
        final String messagePayload = santaMessageFormatter.format(giftGiver, giftReceiver);

        final ImmutableSmsInput smsInput = ImmutableSmsInput.builder()
                .phoneNumber(phoneNumber)
                .messagePayload(messagePayload)
                .build();

        return messenger.sendSms(smsInput);
    }
}
