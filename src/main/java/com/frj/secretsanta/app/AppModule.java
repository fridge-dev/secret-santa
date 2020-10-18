package com.frj.secretsanta.app;

import com.frj.secretsanta.app.internal.assignment.DefaultSecretSantaAssigner;
import com.frj.secretsanta.app.internal.sms.NoOpSmsMessenger;
import org.immutables.value.Value;

@Value.Immutable
public interface AppModule {

    SecretSantaService getSecretSantaService();

    static AppModule newInstance() {
        SecretSantaService secretSantaService = new DefaultSecretSantaService(
                new DefaultSecretSantaAssigner(),
                new NoOpSmsMessenger()
        );

        return ImmutableAppModule.builder()
                .secretSantaService(secretSantaService)
                .build();
    }
}
