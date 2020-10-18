package com.frj.secretsanta.app;

public class NoOpSecretSantaHandler implements SecretSantaMessenger {
    @Override
    public SecretSantaOutput handle(final SecretSantaInput input) {
        return new SecretSantaOutput();
    }
}
