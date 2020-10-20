package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;

import java.util.Map;
import java.util.Set;

/**
 * Generic message formatting logic
 */
public class RawMessageFormatter {

    private final String messageFormat;

    private final Set<String> paramKeys;

    private RawMessageFormatter(String messageFormat, Set<String> paramKeys) {
        this.messageFormat = messageFormat;
        this.paramKeys = paramKeys;
    }

    public static RawMessageFormatter create(final String messageFormat) throws ClientException {
        final Set<String> params = MessageParser.parseParamKeys(messageFormat);
        return new RawMessageFormatter(messageFormat, params);
    }

    public String format(final Map<String, String> paramValues) {
        if (!paramKeys.equals(paramValues.keySet())) {
            throw new IllegalArgumentException("Illegal set of message format params.");
        }

        String formattedMessage = messageFormat;
        for (String paramKey : paramKeys) {
            formattedMessage = formattedMessage.replaceAll(String.format("{%s}", paramKey), paramValues.get(paramKey));
        }

        return formattedMessage;
    }

    public boolean areParamsValid(final Set<String> paramKeys) {
        return this.paramKeys.equals(paramKeys);
    }
}
