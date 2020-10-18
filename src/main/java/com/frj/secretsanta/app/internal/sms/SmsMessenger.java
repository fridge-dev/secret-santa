package com.frj.secretsanta.app.internal.sms;

import org.immutables.value.Value;

public interface SmsMessenger {

    /**
     * @return true if successful
     */
    boolean sendSms(SmsInput input);

    @Value.Immutable
    interface SmsInput {
        String phoneNumber();
        String messagePayload();
    }
}
