package com.frj.secretsanta.lambda;

import com.frj.secretsanta.app.AppModule;

/**
 * Ain't need no DI framework.
 */
public class LambdaModule {

    public static SecretSantaLambdaHandler createHandler() {
        AppModule appModule = AppModule.newInstance();
        return new SecretSantaLambdaHandler(appModule.getMessenger());
    }

}
