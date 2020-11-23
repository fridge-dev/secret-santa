package com.frj.secretsanta.lambda;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.frj.secretsanta.app.AppModule;
import com.frj.secretsanta.app.internal.AppExternalDeps;
import com.frj.secretsanta.app.internal.ImmutableAppExternalDeps;

/**
 * Ain't need no DI framework.
 */
public class LambdaModule {

    public static SecretSantaLambdaHandler createHandler() {
        AppExternalDeps appExternalDeps = createExternalDependencies();
        AppModule appModule = AppModule.newInstance(appExternalDeps);
        return new SecretSantaLambdaHandler(appModule.getSecretSantaService());
    }

    private static AppExternalDeps createExternalDependencies() {
        AmazonSNS sns = AmazonSNSClient.builder()
                // TODO configure AWS client?
                .build();

        return ImmutableAppExternalDeps.builder()
                .amazonSNS(sns)
                .build();
    }

}
