package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RawMessageFormatterTest {

    @Test
    void format() throws ClientException {
        final RawMessageFormatter f = RawMessageFormatter.create("Hello {recipient}, you are {target}'s santa.");

        final String formatted = f.format(Map.of("recipient", "you", "target", "me"));

        assertEquals("Hello you, you are me's santa.", formatted);
    }

    @Test
    void cantUsePlainDollarSign() throws ClientException {
        // -- setup --
        final RawMessageFormatter f = RawMessageFormatter.create("I want {present}");
        final Map<String, String> badParam = Map.of("present", "something $20");
        final Map<String, String> goodParam = Map.of("present", "something \\$20");

        // -- execute & verify --
        assertThrows(IndexOutOfBoundsException.class, () -> f.format(badParam));
        assertEquals("I want something $20", f.format(goodParam));
    }
}