package com.frj.secretsanta.app.internal.format;

import com.frj.secretsanta.app.api.ClientException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RawMessageFormatterTest {

    @Test
    void format() throws ClientException {
        final RawMessageFormatter f = RawMessageFormatter.create("Hello {recipient}, you are {target}'s santa.");

        final String formatted = f.format(Map.of("recipient", "you", "target", "me"));

        assertEquals("Hello you, you are me's santa.", formatted);
    }
}