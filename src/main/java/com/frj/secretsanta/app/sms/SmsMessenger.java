package com.frj.secretsanta.app.sms;

import org.immutables.value.Value;

public interface SmsMessenger {

    void sendSms(Input input);

    @Value.Immutable
    interface Input {
        String recipient();
        String messagePayload();
    }
}
