package com.frj.secretsanta.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.frj.secretsanta.app.SecretSantaService;
import com.frj.secretsanta.app.api.ClientException;
import com.frj.secretsanta.app.api.Exclusion;
import com.frj.secretsanta.app.api.ImmutablePersonData;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SecretSantaLambdaHandler implements RequestHandler<SecretSantaLambdaRequest, SecretSantaLambdaReply> {

    private static final Logger log = LogManager.getLogger(SecretSantaLambdaHandler.class);

    private final SecretSantaService secretSantaService;

    public SecretSantaLambdaHandler(final SecretSantaService secretSantaService) {
        this.secretSantaService = Objects.requireNonNull(secretSantaService);
    }

    @Override
    public SecretSantaLambdaReply handleRequest(final SecretSantaLambdaRequest lambdaRequest, final Context context) {
        final long start = System.nanoTime();
        try {
            return doHandle(lambdaRequest);
        } finally {
            final long duration = System.nanoTime() - start;
            System.out.printf("Timer#LambdaHandler=%dms%n", TimeUnit.NANOSECONDS.toMillis(duration));
        }
    }

    private SecretSantaLambdaReply doHandle(final SecretSantaLambdaRequest lambdaRequest) {
        final SecretSantaBroadcastOutput appOutput;

        try {
            final SecretSantaBroadcastInput appInput = convertRequest(lambdaRequest);
            appOutput = secretSantaService.broadcastMessage(appInput);
        } catch (ClientException e) {
            log.info("Invalid input from client, failed to broadcast message.", e);
            SecretSantaLambdaReply reply = new SecretSantaLambdaReply();
            reply.setStatus("4xx");
            reply.setErrorMessage(e.getMessage());
            return reply;
        } catch (ServiceException | RuntimeException e) {
            log.error("Internal error while broadcasting message.", e);
            SecretSantaLambdaReply reply = new SecretSantaLambdaReply();
            reply.setStatus("5xx");
            reply.setErrorMessage(e.getClass().getSimpleName() + " - " + e.getMessage());
            return reply;
        }

        return convertReply(appOutput);
    }

    private SecretSantaBroadcastInput convertRequest(final SecretSantaLambdaRequest lambdaRequest) throws ClientException {
        final ImmutableSecretSantaBroadcastInput.Builder input = ImmutableSecretSantaBroadcastInput.builder();

        // Person data
        final Set<String> personIds = new HashSet<>();
        for (Map<String, String> personData : require(lambdaRequest.getPeopleData(), "peopleData")) {
            final String personId = personData.get("id");
            if (null == personId) {
                throw new ClientException("Missing required person 'id'");
            }
            if (!personIds.add(personId)) {
                throw new ClientException("Duplicate person " + personId);
            }

            input.addPeople(ImmutablePersonData.builder()
                    .messageData(personData)
                    .build());
        }

        // Person IDs to message
        if (lambdaRequest.getPersonIdsToMessage() != null && lambdaRequest.getPersonIdsToMessage().size() > 0) {
            input.personIdsToMessage(lambdaRequest.getPersonIdsToMessage());
        } else {
            input.personIdsToMessage(personIds);
        }

        // Exclusions
        for (String[] exclusionRule : require(lambdaRequest.getExclusionRules(), "exclusionRules")) {
            if (exclusionRule.length != 2) {
                throw new ClientException("Illegal exclusion rule format");
            }
            final String id1 = exclusionRule[0];
            final String id2 = exclusionRule[1];
            if (!personIds.containsAll(Arrays.asList(id1, id2))) {
                throw new ClientException("Unrecognized personId in exclusion rule: " + Arrays.toString(exclusionRule));
            }

            input.addExclusions(Exclusion.of(id1, id2));
        }

        // Simple stuff
        input.messageFormat(require(lambdaRequest.getMessageFormat(), "messageFormat"));
        input.rngSeed(lambdaRequest.getRngSeed());

        return input.build();
    }

    private <T> T require(final T obj, final String fieldName) throws ClientException {
        if (obj == null) {
            throw new ClientException("Missing required field " + fieldName);
        }

        return obj;
    }

    private SecretSantaLambdaReply convertReply(final SecretSantaBroadcastOutput appOutput) {
        if (appOutput.failedPersonIds().isEmpty()) {
            return SecretSantaLambdaReply.success();
        }

        final SecretSantaLambdaReply reply = new SecretSantaLambdaReply();
        reply.setStatus("5xx");
        reply.setErrorMessage("Partial failure to publish SMS");
        reply.setFailedIndividuals(appOutput.failedPersonIds());

        return reply;
    }
}
