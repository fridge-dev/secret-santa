package com.frj.secretsanta.app.api;

import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
public interface SecretSantaBroadcastOutput {

    Set<String> failedPersonIds();

}
