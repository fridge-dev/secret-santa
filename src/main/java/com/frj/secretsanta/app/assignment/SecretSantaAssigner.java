package com.frj.secretsanta.app.assignment;

import org.immutables.value.Value;

import java.util.List;
import java.util.Set;

public interface SecretSantaAssigner {

    Output getAssignments(Input input);

    @Value.Immutable
    interface Input {
        Set<String> personIds();
        List<Exclusion> exclusions();
        long rngSeed();
    }

    @Value.Immutable
    interface Exclusion {
        String personId1();
        String personId2();
    }

    @Value.Immutable
    interface Output {
        List<Assignment> assignments();
    }

    @Value.Immutable
    interface Assignment {
        String giverPersonId();
        String receiverPersonId();
    }
}
