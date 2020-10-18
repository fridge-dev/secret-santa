package com.frj.secretsanta.app;

import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;

final class NoOpSecretSantaService implements SecretSantaService {
    @Override
    public SecretSantaBroadcastOutput broadcastMessage(final SecretSantaBroadcastInput input) {
        return ImmutableSecretSantaBroadcastOutput.builder()
                .build();
    }
}
