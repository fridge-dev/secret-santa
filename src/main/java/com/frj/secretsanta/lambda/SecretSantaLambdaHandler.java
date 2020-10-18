package com.frj.secretsanta.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.frj.secretsanta.app.SecretSantaMessenger;
import com.frj.secretsanta.app.SecretSantaInput;
import com.frj.secretsanta.app.SecretSantaOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SecretSantaLambdaHandler implements RequestHandler<SecretSantaLambdaRequest, SecretSantaLambdaReply> {

    private static final Logger log = LogManager.getLogger();

    private final SecretSantaMessenger secretSantaMessenger;

    public SecretSantaLambdaHandler(final SecretSantaMessenger secretSantaMessenger) {
        this.secretSantaMessenger = Objects.requireNonNull(secretSantaMessenger);
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
        final SecretSantaInput appInput = convertRequest(lambdaRequest);
        final SecretSantaOutput appOutput = secretSantaMessenger.handle(appInput);
        return convertReply(appOutput);
    }

    private SecretSantaInput convertRequest(final SecretSantaLambdaRequest lambdaRequest) {
        // TODO
        return new SecretSantaInput();
    }

    private SecretSantaLambdaReply convertReply(final SecretSantaOutput appOutput) {
        // TODO
        return new SecretSantaLambdaReply();
    }
}
