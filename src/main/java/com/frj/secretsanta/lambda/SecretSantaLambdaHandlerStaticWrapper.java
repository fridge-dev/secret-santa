package com.frj.secretsanta.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * The whole point of this class is to instantiate the actual handler statically so it
 * is created during function initialization.
 */
public class SecretSantaLambdaHandlerStaticWrapper implements RequestHandler<SecretSantaLambdaRequest, SecretSantaLambdaReply> {

    private static final RequestHandler<SecretSantaLambdaRequest, SecretSantaLambdaReply> DELEGATE = LambdaModule.createHandler();

    @Override
    public SecretSantaLambdaReply handleRequest(final SecretSantaLambdaRequest lambdaRequest, final Context context) {
        return DELEGATE.handleRequest(lambdaRequest, context);
    }
}
