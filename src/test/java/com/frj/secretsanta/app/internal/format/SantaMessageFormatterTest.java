package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;
import com.frj.secretsanta.app.api.ImmutablePersonData;
import com.frj.secretsanta.app.api.PersonData;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SantaMessageFormatterTest {

    @Test
    void formatSanityTest() throws ClientException {
        // -- setup --
        final SantaMessageFormatter f = SantaMessageFormatter.create("Hello {self.id}, you have been assigned {target.id}. Gift ideas: \"{target.giftIdeas}\"");

        final PersonData giver = ImmutablePersonData.builder()
                .messageData(Map.of(
                        "id", "Wayne",
                        "phoneNumber", "12341234",
                        "giftIdeas", "vibeo games"
                ))
                .build();

        final PersonData receiver = ImmutablePersonData.builder()
                .messageData(Map.of(
                        "id", "Arton",
                        "phoneNumber", "33333",
                        "giftIdeas", "gimme chocolate"
                ))
                .build();

        // -- execute --
        final String payload = f.format(giver, receiver);

        // -- verify --
        assertEquals(
                "Hello Wayne, you have been assigned Arton. Gift ideas: \"gimme chocolate\"",
                payload
        );
    }
}