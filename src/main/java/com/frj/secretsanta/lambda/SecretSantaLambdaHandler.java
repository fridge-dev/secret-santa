package com.frj.secretsanta.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.frj.secretsanta.app.SecretSantaService;
import com.frj.secretsanta.app.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.SecretSantaBroadcastOutput;
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
        final SecretSantaBroadcastOutput appOutput = secretSantaService.broadcastMessage(appInput);
        return convertReply(appOutput);
    }

    private SecretSantaBroadcastInput convertRequest(final SecretSantaLambdaRequest lambdaRequest) {
        // TODO
        return new SecretSantaBroadcastInput();
    }

    private SecretSantaLambdaReply convertReply(final SecretSantaBroadcastOutput appOutput) {
        // TODO
        return new SecretSantaLambdaReply();
    }
}
