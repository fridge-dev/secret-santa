package com.frj.secretsanta.app;

public class NoOpSecretSanta implements SecretSanta {
    @Override
    public SecretSantaBroadcastOutput broadcastMessage(final SecretSantaBroadcastInput input) {
        return new SecretSantaBroadcastOutput();
    }
}
