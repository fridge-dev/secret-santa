package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;
import com.frj.secretsanta.app.api.PersonData;

import java.text.MessageFormat;

/**
 * Secret santa specific formatting logic (i.e. uses {@link PersonData}).
 */
public class SantaMessageFormatter {

    private final RawMessageFormatter messageFormatter;

    private SantaMessageFormatter(RawMessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;
    }

    public static SantaMessageFormatter create(final String messageFormat) throws ClientException {
        return new SantaMessageFormatter(RawMessageFormatter.create(messageFormat));
    }

    public String format(final PersonData giftGiver, final PersonData giftReceiver) {
        // TODO implement properly & test
        return messageFormatter.format(giftGiver.messageData());
    }

    public void performPreValidation(final PersonData person) throws ClientException {
        // TODO implement properly & test
        if (!messageFormatter.areParamsValid(person.messageData().keySet())) {
            throw new ClientException(MessageFormat.format("Person {0} has missing or extra params", person.personId()));
        }
    }
}
