package com.frj.secretsanta.app;

import java.util.Objects;

public final class AppModule {

    public static AppModule newInstance() {
        // TODO use real stuff
        return new AppModule(new NoOpSecretSantaService());
    }

    private final SecretSantaService secretSantaService;

    private AppModule(final SecretSantaService secretSantaService) {
        // No dependencies, yet
        this.secretSantaService = Objects.requireNonNull(secretSantaService);
    }

    public SecretSantaService getSecretSantaService() {
        return secretSantaService;
    }
}
