package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;

import java.util.Map;
import java.util.Set;

/**
 * Generic message formatting logic.
 *
 * An instance of this is specific to a message format, not to a specific person.
 */
class RawMessageFormatter {

    private final String messageFormat;

    private final Set<String> messageFormatKeys;

    private RawMessageFormatter(String messageFormat, Set<String> messageFormatKeys) {
        this.messageFormat = messageFormat;
        this.messageFormatKeys = messageFormatKeys;
    }

    public static RawMessageFormatter create(final String messageFormat) throws ClientException {
        final Set<String> params = MessageParser.parseParamKeys(messageFormat);
        return new RawMessageFormatter(messageFormat, params);
    }

    public String format(final Map<String, String> paramsForPerson) throws ClientException {
        if (!paramsForPerson.keySet().containsAll(messageFormatKeys)) {
            throw new ClientException("Illegal set of message format params.");
        }

        String formattedMessage = messageFormat;
        for (String key : messageFormatKeys) {
            formattedMessage = formattedMessage.replaceAll(String.format("\\{%s}", key), paramsForPerson.get(key));
        }

        return formattedMessage;
    }
}
