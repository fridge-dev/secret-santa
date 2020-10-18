package com.frj.secretsanta.app.internal.sms;

public class NoOpSmsMessenger implements SmsMessenger {
    @Override
    public boolean sendSms(SmsInput input) {
        return false;
    }
}
