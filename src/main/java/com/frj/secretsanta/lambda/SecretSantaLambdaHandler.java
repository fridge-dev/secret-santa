package com.frj.secretsanta.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.frj.secretsanta.app.SecretSantaService;
import com.frj.secretsanta.app.api.ClientException;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SecretSantaLambdaHandler implements RequestHandler<SecretSantaLambdaRequest, SecretSantaLambdaReply> {

    private static final Logger log = LogManager.getLogger();

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
            log.info("Timer#LambdaHandler={}ms", TimeUnit.NANOSECONDS.toMillis(duration));
        }
    }

    private SecretSantaLambdaReply doHandle(final SecretSantaLambdaRequest lambdaRequest) {
        final SecretSantaBroadcastInput appInput = convertRequest(lambdaRequest);
        final SecretSantaBroadcastOutput appOutput;

        try {
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
            reply.setErrorMessage(e.getMessage());
            return reply;
        }

        return convertReply(appOutput);
    }

    private SecretSantaBroadcastInput convertRequest(final SecretSantaLambdaRequest lambdaRequest) {
        // TODO
        return ImmutableSecretSantaBroadcastInput.builder()
                .build();
    }

    private SecretSantaLambdaReply convertReply(final SecretSantaBroadcastOutput appOutput) {
        if (appOutput.failedPersonIds().isEmpty()) {
            SecretSantaLambdaReply.success();
        }

        final SecretSantaLambdaReply reply = new SecretSantaLambdaReply();
        reply.setStatus("5xx");
        reply.setErrorMessage("Partial failure to publish SMS");
        reply.setFailedIndividuals(appOutput.failedPersonIds());

        return reply;
    }
}
