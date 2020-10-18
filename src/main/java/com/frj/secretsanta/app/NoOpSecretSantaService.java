package com.frj.secretsanta.app;

public class NoOpSecretSantaService implements SecretSantaService {
    @Override
    public SecretSantaBroadcastOutput broadcastMessage(final SecretSantaBroadcastInput input) {
        return new SecretSantaBroadcastOutput();
    }
}
