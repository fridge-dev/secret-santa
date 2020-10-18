package com.frj.secretsanta.app;

import org.immutables.value.Value;

@Value.Immutable
public interface AppModule {

    SecretSantaService getSecretSantaService();

    static AppModule newInstance() {
        // TODO use real stuff
        NoOpSecretSantaService secretSantaService = new NoOpSecretSantaService();

        return ImmutableAppModule.builder()
                .secretSantaService(secretSantaService)
                .build();
    }
}
