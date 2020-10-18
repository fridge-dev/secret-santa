package com.frj.secretsanta.app;

import com.frj.secretsanta.app.api.ClientException;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.PersonData;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.internal.assignment.ImmutableAssignmentInput;
import com.frj.secretsanta.app.internal.assignment.SecretSantaAssigner;
import com.frj.secretsanta.app.internal.format.MessageFormatter;
import com.frj.secretsanta.app.internal.sms.ImmutableSmsInput;
import com.frj.secretsanta.app.internal.sms.SmsMessenger;

import java.text.MessageFormat;
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

        SecretSantaAssigner.AssignmentOutput assignmentOutput = getAssignments(input);

        final Set<String> failedPersonIds = broadcastSms(input, assignmentOutput.assignments());

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
//        final Set<String> personIds = new HashSet<>(input.allPersonIds());
//        final Set<String> phoneNumbers = new HashSet<>();
//        for (PersonData person : input.people()) {
//            if (!personIds.add(person.personId())) {
//                throw new ClientException(MessageFormat.format("People contains duplicate: '{0}'", person.personId()));
//            }
//            if (!phoneNumbers.add(person.phoneNumber())) {
//                throw new ClientException(MessageFormat.format("Duplicate phone number: '{0}'", person.phoneNumber()));
//            }
//        }

        // Validate personIdsToMessage is subset of people
        for (String personIdToMessage : input.personIdsToMessage()) {
            if (input.allPersonIds().contains(personIdToMessage)) {
                throw new ClientException(MessageFormat.format("PersonIdToMessage '{0}' is missing in people list", personIdToMessage));
            }
        }

        final Set<String> params = MessageFormatter.getParams(input.messageFormat());
        for (PersonData person : input.people()) {
            if (!params.equals(person.messageData().keySet())) {
                throw new ClientException(MessageFormat.format("Person {0} has missing or extra params", person.personId()));
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

    private Set<String> broadcastSms(final SecretSantaBroadcastInput serviceInput, final List<SecretSantaAssigner.Assignment> assignments) {
        final Set<String> failedPersonIds = new HashSet<>();
        for (SecretSantaAssigner.Assignment assignment : assignments) {
            final PersonData giftGiver = serviceInput.peopleByPersonId().get(assignment.giverPersonId());
            final PersonData giftReceiver = serviceInput.peopleByPersonId().get(assignment.receiverPersonId());
            if (!serviceInput.personIdsToMessage().contains(giftGiver.personId())) {
                continue;
            }

            boolean success = sendSms(giftGiver, giftReceiver, serviceInput.messageFormat());

            if (!success) {
                failedPersonIds.add(assignment.giverPersonId());
            }
        }
        return failedPersonIds;
    }

    private boolean sendSms(final PersonData giftGiver, final PersonData giftReceiver, final String messageFormat) {
        final String phoneNumber = giftGiver.phoneNumber();
        final String messagePayload = MessageFormatter.format(giftGiver, giftReceiver, messageFormat);

        ImmutableSmsInput smsInput = ImmutableSmsInput.builder()
                .phoneNumber(phoneNumber)
                .messagePayload(messagePayload)
                .build();

        return messenger.sendSms(smsInput);
    }
}
