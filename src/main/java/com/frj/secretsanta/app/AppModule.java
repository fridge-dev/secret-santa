package com.frj.secretsanta.app;

import com.frj.secretsanta.app.internal.AppExternalDeps;
import com.frj.secretsanta.app.internal.assignment.BruteForceSecretSantaAssigner;
import com.frj.secretsanta.app.internal.sms.SmsModule;
import org.immutables.value.Value;

@Value.Immutable
public interface AppModule {

    SecretSantaService getSecretSantaService();

    static AppModule newInstance(final AppExternalDeps appExternalDeps) {
        SmsModule smsModule = SmsModule.newInstance(appExternalDeps.amazonSNS());

        SecretSantaService secretSantaService = new DefaultSecretSantaService(
                new BruteForceSecretSantaAssigner(),
                smsModule.getSmsMessenger()
        );

        return ImmutableAppModule.builder()
                .secretSantaService(secretSantaService)
                .build();
    }
}
