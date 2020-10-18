package com.frj.secretsanta.app.api;

import org.immutables.value.Value;

@Value.Immutable
public interface Exclusion {
    String personId1();
    String personId2();
}
