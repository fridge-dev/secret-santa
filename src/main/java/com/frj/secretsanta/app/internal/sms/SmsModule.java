package com.frj.secretsanta.app.internal.sms;

import com.amazonaws.services.sns.AmazonSNS;
import org.immutables.value.Value;

@Value.Immutable
public interface SmsModule {

    SmsMessenger getSmsMessenger();

    static SmsModule newInstance(AmazonSNS sns) {
        final SmsMessenger smsMessenger = new AwsSnsSmsMessenger(sns);

        return ImmutableSmsModule.builder()
                .smsMessenger(smsMessenger)
                .build();
    }
}
