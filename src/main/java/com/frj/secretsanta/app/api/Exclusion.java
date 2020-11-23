package com.frj.secretsanta.app.api;

import org.immutables.value.Value;

@Value.Immutable
public interface Exclusion {

    String personId1();
    String personId2();

    @Value.Derived
    default boolean equalsIgnoreOrder(final String personId1, final String personId2) {
        final String p1 = personId1();
        final String p2 = personId2();

        if (p1.equals(personId1)) {
            return p2.equals(personId2);
        } else if (p2.equals(personId1)) {
            return p1.equals(personId2);
        } else {
            return false;
        }
    }

    static Exclusion of(final String personId1, final String personId2) {
        return ImmutableExclusion.builder()
                .personId1(personId1)
                .personId2(personId2)
                .build();
    }
}
