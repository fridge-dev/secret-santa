package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;
import com.frj.secretsanta.app.api.PersonData;

import java.util.HashMap;
import java.util.Map;

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

    public String format(final PersonData giftGiver, final PersonData giftReceiver) throws ClientException {
        final Map<String, String> params = new HashMap<>();
        giftGiver.messageData().forEach((k, v) -> params.put("self." + k, v));
        giftReceiver.messageData().forEach((k, v) -> params.put("target." + k, v));

        return messageFormatter.format(params);
    }
}
