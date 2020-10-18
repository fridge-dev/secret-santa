package com.frj.secretsanta.app.internal.assignment;

import com.frj.secretsanta.app.api.Exclusion;
import org.immutables.value.Value;

import java.util.List;

public interface SecretSantaAssigner {

    AssignmentOutput getAssignments(AssignmentInput input);

    @Value.Immutable
    interface AssignmentInput {
        List<String> personIds();
        List<Exclusion> exclusions();
        long rngSeed();
    }

    @Value.Immutable
    interface AssignmentOutput {
        List<Assignment> assignments();
    }

    @Value.Immutable
    interface Assignment {
        String giverPersonId();
        String receiverPersonId();
    }
}
