package com.frj.secretsanta.app.api;

import java.util.Map;

public class ServiceApiTestUtils {

    public static PersonData makePerson(
            final String personId,
            final String phoneNumber,
            final Map<String, String> messageData
    ) {
        return ImmutablePersonData.builder()
                .messageData(messageData)
                .putMessageData("id", personId)
                .putMessageData("phoneNumber", phoneNumber)
                .build();
    }
}