package com.frj.secretsanta.app.api;

import org.immutables.value.Value;

import java.util.List;
import java.util.Map;

@Value.Immutable
public interface SecretSantaBroadcastInput {
    String messageFormat();
    Map<String, PersonData> personDataById();
    List<Exclusion> exclusions();
    long rngSeed();
}
