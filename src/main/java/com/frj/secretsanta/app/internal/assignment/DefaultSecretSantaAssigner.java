package com.frj.secretsanta.app.internal.assignment;

import java.util.Arrays;

public class DefaultSecretSantaAssigner implements SecretSantaAssigner {

    @Override
    public Output getAssignments(final Input input) {
        Assignment assignment1 = ImmutableAssignment.builder()
                .giverPersonId("hello")
                .receiverPersonId("world")
                .build();

        Assignment assignment2 = ImmutableAssignment.builder()
                .giverPersonId("hello")
                .receiverPersonId("world")
                .build();

        return ImmutableOutput.builder()
                .assignments(Arrays.asList(assignment1, assignment2))
                .build();
    }
}
