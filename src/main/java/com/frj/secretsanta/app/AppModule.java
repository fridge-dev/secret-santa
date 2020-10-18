package com.frj.secretsanta.app;

import java.util.Objects;

public final class AppModule {

    public static AppModule newInstance() {
        return new AppModule(new NoOpSecretSantaHandler());
    }

    private final SecretSantaMessenger messenger;

    private AppModule(final SecretSantaMessenger messenger) {
        // No dependencies, yet
        this.messenger = Objects.requireNonNull(messenger);
    }

    public SecretSantaMessenger getMessenger() {
        return messenger;
    }
}
