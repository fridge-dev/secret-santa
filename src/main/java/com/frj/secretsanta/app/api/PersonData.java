package com.frj.secretsanta.app.api;

import org.immutables.value.Value;

import java.util.Map;

@Value.Immutable
public interface PersonData {

    Map<String, String> messageData();

    @Value.Check
    default void checkRequiredDerivedFields() {
        if (null == personId()) {
            throw new IllegalArgumentException("Missing required field: 'id'");
        }
        if (null == phoneNumber()) {
            throw new IllegalArgumentException("Missing required field: 'phoneNumber'");
        }
    }

    @Value.Derived
    default String personId() {
        return messageData().get("id");
    }

    @Value.Derived
    default String phoneNumber() {
        return messageData().get("phoneNumber");
    }
}
