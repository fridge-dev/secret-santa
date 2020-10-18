package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageFormatterTest {

    @Test
    void getParams() throws Exception {
        // -- setup --

        // -- execute --
        Set<String> actual = MessageFormatter.getParams("Hello {recipient}, you are {target}'s santa.");

        // -- verify --
        HashSet<String> expected = new HashSet<>(Arrays.asList("recipient", "target"));
        assertEquals(expected, actual);
    }

    @Test
    void getParams_Bounds() throws Exception {
        // -- setup --

        // -- execute --
        Set<String> actual = MessageFormatter.getParams("{recipient} you have {target}");

        // -- verify --
        HashSet<String> expected = new HashSet<>(Arrays.asList("recipient", "target"));
        assertEquals(expected, actual);
    }

    @Test
    void getParams_Dupe() throws Exception {
        // -- setup --

        // -- execute --
        Set<String> actual = MessageFormatter.getParams("{recipient} you have {target}, {recipient} you have {target}");

        // -- verify --
        HashSet<String> expected = new HashSet<>(Arrays.asList("recipient", "target"));
        assertEquals(expected, actual);
    }

    @Test
    void getParams_Illegal() throws Exception {
        assertInvalid("Bad: {recip{ient}");
        assertInvalid("We {can't do this either}");
        assertInvalid("No no no! {}");
        assertInvalid("Also bad: }");
    }

    private void assertInvalid(final String message) {
        assertThrows(ClientException.class, () -> MessageFormatter.getParams(message));
    }
}