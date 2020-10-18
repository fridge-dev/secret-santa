package com.frj.secretsanta.app;

import java.util.Objects;

public final class AppModule {

    public static AppModule newInstance() {
        // TODO use real stuff
        return new AppModule(new NoOpSecretSanta());
    }

    private final SecretSanta secretSanta;

    private AppModule(final SecretSanta secretSanta) {
        // No dependencies, yet
        this.secretSanta = Objects.requireNonNull(secretSanta);
    }

    public SecretSanta getSecretSanta() {
        return secretSanta;
    }
}
