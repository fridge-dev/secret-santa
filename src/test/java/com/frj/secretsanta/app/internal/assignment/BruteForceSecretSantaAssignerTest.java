package com.frj.secretsanta.app.internal.assignment;

import com.frj.secretsanta.app.api.Exclusion;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BruteForceSecretSantaAssignerTest {

    @Test
    void basicTestCases() {
        final List<SecretSantaAssigner.AssignmentInput> inputsToTest = Arrays.asList(
                // Without exclusions
                makeInput(Arrays.asList("p1", "p2")),
                makeInput(Arrays.asList("p1", "p2", "p3")),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4")),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4", "p5")),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6")),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6", "p7")),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8")),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9")),

                // With exclusions
                makeInput(Arrays.asList("p1", "p2", "p3", "p4"), Arrays.asList(Exclusion.of("p1", "p2"))),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4"), Arrays.asList(Exclusion.of("p1", "p3"))),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4"), Arrays.asList(Exclusion.of("p2", "p3"))),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4"), Arrays.asList(Exclusion.of("p1", "p2"), Exclusion.of("p3", "p4"))),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4", "p5"), Arrays.asList(Exclusion.of("p1", "p2"), Exclusion.of("p1", "p3"))),
                makeInput(Arrays.asList("p1", "p2", "p3", "p4", "p5"), Arrays.asList(Exclusion.of("p1", "p2"), Exclusion.of("p3", "p4"))),

                // Epic
                makeInput(
                        Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9"),
                        Arrays.asList(
                                Exclusion.of("p1", "p2"),
                                Exclusion.of("p3", "p4"),
                                Exclusion.of("p5", "p6"),
                                Exclusion.of("p7", "p8")
                        )
                )
        );


        inputsToTest.forEach(this::basicTestCase);
    }

    private SecretSantaAssigner.AssignmentInput makeInput(final List<String> personIds) {
        return makeInput(personIds, Collections.emptyList());
    }

    private SecretSantaAssigner.AssignmentInput makeInput(final List<String> personIds, final List<Exclusion> exclusions) {
        return ImmutableAssignmentInput.builder()
                .personIds(personIds)
                .exclusions(exclusions)
                .rngSeed(1L)
                .build();
    }

    private void basicTestCase(final SecretSantaAssigner.AssignmentInput input) {
        final SecretSantaAssigner assigner = new BruteForceSecretSantaAssigner();
        final SecretSantaAssigner.AssignmentOutput output = assigner.getAssignments(input);

        assertBasicAssignmentRules(input, output);
    }

    @Test
    void deterministicRng() {
        // -- setup --
        final SecretSantaAssigner.AssignmentInput baseInput = ImmutableAssignmentInput.builder()
                .personIds(Arrays.asList("p1", "p2", "p3", "p4", "p5"))
                .exclusions(Arrays.asList(Exclusion.of("p1", "p2"), Exclusion.of("p3", "p4")))
                .rngSeed(1L)
                .build();

        final SecretSantaAssigner.AssignmentInput input1 = copyAndSetRngSeed(baseInput, 1);
        final SecretSantaAssigner.AssignmentInput input2 = copyAndSetRngSeed(baseInput, 2);
        final SecretSantaAssigner.AssignmentInput input3 = copyAndSetRngSeed(baseInput, 3);

        // -- execute --
        final SecretSantaAssigner assigner = new BruteForceSecretSantaAssigner();
        final SecretSantaAssigner.AssignmentOutput output1a = assigner.getAssignments(input1);
        final SecretSantaAssigner.AssignmentOutput output1b = assigner.getAssignments(input1);
        final SecretSantaAssigner.AssignmentOutput output2a = assigner.getAssignments(input2);
        final SecretSantaAssigner.AssignmentOutput output2b = assigner.getAssignments(input2);
        final SecretSantaAssigner.AssignmentOutput output3a = assigner.getAssignments(input3);
        final SecretSantaAssigner.AssignmentOutput output3b = assigner.getAssignments(input3);

        // -- verify --
        assertEquals(output1a, output1b);
        assertEquals(output2a, output2b);
        assertEquals(output3a, output3b);

        assertNotEquals(output1a, output2a);
        assertNotEquals(output1a, output3a);
    }

    private SecretSantaAssigner.AssignmentInput copyAndSetRngSeed(final SecretSantaAssigner.AssignmentInput input, final long rngSeed) {
        return ImmutableAssignmentInput.builder()
                .from(input)
                .rngSeed(rngSeed)
                .build();
    }

    private void assertBasicAssignmentRules(
            final SecretSantaAssigner.AssignmentInput input,
            final SecretSantaAssigner.AssignmentOutput output
    ) {
        final Set<String> givers = new HashSet<>();
        final Set<String> receivers = new HashSet<>();

        for (SecretSantaAssigner.Assignment assignment : output.assignments()) {
            final String giver = assignment.giverPersonId();
            final String receiver = assignment.receiverPersonId();

            assertNotEquals(giver, receiver, "Not allowed to give to self");
            assertTrue(givers.add(giver), "Duplicate giver");
            assertTrue(receivers.add(receiver), "Duplicate receiver");

            final boolean exclusionViolated = input.exclusions()
                    .stream()
                    .anyMatch(exclusion -> exclusion.equalsIgnoreOrder(giver, receiver));
            assertFalse(exclusionViolated);
        }

        assertEquals(new HashSet<>(input.personIds()), givers, "Givers is not set-equal to input");
        assertEquals(new HashSet<>(input.personIds()), receivers, "Receivers is not set-equal to input");
    }
}