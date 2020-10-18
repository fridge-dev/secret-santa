package com.frj.secretsanta.app.api;

import org.immutables.value.Value;

import java.util.Map;

@Value.Immutable
public interface PersonData {
    String personId();
    String phoneNumber();
    Map<String, String> messageData();
}
