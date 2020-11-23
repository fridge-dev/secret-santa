package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MessageParser {

    private static final Pattern MESSAGE_PARAMS = Pattern.compile("\\{[a-zA-Z.]+}");

    public static Set<String> parseParamKeys(final String message) throws ClientException {
        List<String> params = new ArrayList<>();
        Matcher matcher = MESSAGE_PARAMS.matcher(message);
        while (matcher.find()) {
            String param = matcher.group(0);
            // Trim '{' and '}'
            param = param.substring(1, param.length() - 1);
            params.add(param);
        }

        validateAllCurliesAreMatched(message, params);

        return new HashSet<>(params);
    }

    /**
     * This is just because we don't want any stray curly brackets, so we don't have to
     * try to guess how that might end up being handled.
     */
    private static void validateAllCurliesAreMatched(String message, List<String> params) throws ClientException {
        int lCurlCount = findNumOccurrencesOfSubstring(message, "{");
        int rCurlCount = findNumOccurrencesOfSubstring(message, "}");
        int curlCount = params.size();

        if (lCurlCount != curlCount || rCurlCount != curlCount) {
            throw new ClientException("Message format can only contain '{...}' for parameters");
        }
    }

    private static int findNumOccurrencesOfSubstring(final String string, final String substring) {
        int i = 0;
        int numOccurrences = 0;

        while (true) {
            i = string.indexOf(substring, i);

            if (i == -1) {
                break;
            }

            numOccurrences++;
            i += substring.length();
        }

        return numOccurrences;
    }
}
