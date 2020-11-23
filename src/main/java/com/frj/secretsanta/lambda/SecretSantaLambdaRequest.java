package com.frj.secretsanta.lambda;

import java.util.List;
import java.util.Map;

/**
 * JSON:
 *
 * ```
 * {
 *     "messageFormat": "Hello {you}, you are assigned {target}",
 *     "people": {
 *         "Mr. Jackson": {
 *             "phoneNumber": "+12345678900",
 *             "giftIdeas": "Pizza",
 *         },
 *         "Mrs. Jackson": {
 *             "phoneNumber": "+12345678901",
 *             "giftIdeas": "Sandwich",
 *         },
 *         "Mr. Randall": {
 *             "phoneNumber": "+12345678902",
 *             "giftIdeas": "Hiyabusa",
 *         },
 *         "Mrs. Randall": {
 *             "phoneNumber": "+12345678903",
 *             "giftIdeas": "Titanium",
 *         },
 *         "Lloyd": {
 *             "phoneNumber": "+12345678904",
 *             "giftIdeas": "Welp",
 *         },
 *     },
 *     "exclusionRules": [
 *         ["Mr. Jackson", "Mrs. Jackson"],
 *         ["Mr. Randall", "Mrs. Randall"],
 *     ],
 *     "rngSeed": 1234,
 * }
 * ```
 */
public class SecretSantaLambdaRequest {

    private String messageFormat;
    private Map<String, Map<String, String>> personDataById;
    private List<String[]> exclusionRules;
    private long rngSeed;
    private List<String> personIdsToMessage;

    public String getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(String messageFormat) {
        this.messageFormat = messageFormat;
    }

    public Map<String, Map<String, String>> getPersonDataById() {
        return personDataById;
    }

    public void setPersonDataById(Map<String, Map<String, String>> personDataById) {
        this.personDataById = personDataById;
    }

    public List<String[]> getExclusionRules() {
        return exclusionRules;
    }

    public void setExclusionRules(List<String[]> exclusionRules) {
        this.exclusionRules = exclusionRules;
    }

    public long getRngSeed() {
        return rngSeed;
    }

    public void setRngSeed(long rngSeed) {
        this.rngSeed = rngSeed;
    }

    public List<String> getPersonIdsToMessage() {
        return personIdsToMessage;
    }

    public void setPersonIdsToMessage(List<String> personIdsToMessage) {
        this.personIdsToMessage = personIdsToMessage;
    }
}
