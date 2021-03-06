package com.frj.secretsanta.app.internal.sms;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;

/**
 * Uses AWS SNS.
 *
 * https://docs.aws.amazon.com/sns/latest/dg/sms_publish-to-phone.html#sms_publish_sdk
 */
public class AwsSnsSmsMessenger implements SmsMessenger {

    private static final Map<String, MessageAttributeValue> SMS_ATTRIBUTES = Map.ofEntries(
            Map.entry("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                    .withDataType("String")
                    // The sender ID shown on the device.
                    .withStringValue("SecretSanta")),
            Map.entry("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                    .withDataType("Number")
                    .withStringValue("1.00")),
            Map.entry("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                    .withDataType("String")
                    // Favor lower price over guaranteed delivery
                    .withStringValue("Promotional"))
    );

    private static final Logger log = LogManager.getLogger(AwsSnsSmsMessenger.class);

    private final AmazonSNS sns;

    public AwsSnsSmsMessenger(final AmazonSNS sns) {
        this.sns = Objects.requireNonNull(sns);
    }

    @Override
    public boolean sendSms(final SmsInput input) {
        System.out.printf("Sending message to %s of length %d%n", input.phoneNumber(), input.messagePayload().length());

        PublishRequest publishRequest = new PublishRequest()
                .withMessageAttributes(SMS_ATTRIBUTES)
                .withMessage(input.messagePayload())
                .withPhoneNumber(input.phoneNumber());

        try {
            PublishResult result = sns.publish(publishRequest);
            System.out.printf("Published to SNS. Result: %s%n", result);
            return true;
        } catch (RuntimeException e) {
            log.error("Failed to publish to SNS for target '{}'.", input.phoneNumber(), e);
            return false;
        }
    }
}
