package com.frj.secretsanta.app.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServiceApiTestUtils {

    public static SecretSantaBroadcastInput makeInput() {
        return ImmutableSecretSantaBroadcastInput.builder()
                .messageFormat("Hello")
                .personIdsToMessage(Arrays.asList("p1", "p2"))
                .people(Arrays.asList(
                        makePerson("p1", "1234", new HashMap<>()),
                        makePerson("p2", "2345", new HashMap<>())
                ))
                .exclusions(Collections.emptyList())
                .rngSeed(1L)
                .build();
    }

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