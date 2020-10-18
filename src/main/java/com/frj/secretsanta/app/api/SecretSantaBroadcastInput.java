package com.frj.secretsanta.app.api;

import org.immutables.value.Value;

import java.util.List;
import java.util.Set;

@Value.Immutable
public interface SecretSantaBroadcastInput {
    String messageFormat();
    Set<String> personIdsToMessage();
    List<PersonData> people();
    List<Exclusion> exclusions();
    long rngSeed();
}
