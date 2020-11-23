package com.frj.secretsanta.app.internal.assignment;

import com.frj.secretsanta.app.api.Exclusion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BruteForceSecretSantaAssigner implements SecretSantaAssigner {

    // Bad algorithm, but it gets the job done. And it was super fast to implement.
    // And it runs fast enough for what I need. So...
    @Override
    public AssignmentOutput getAssignments(final AssignmentInput input) {
        final List<String> assignmentOrder = new ArrayList<>(input.personIds());
        final Random random = new Random(input.rngSeed());
        final List<Assignment> assignments;
        int i = 0;

        // O(n * m) asymptotic runtime. See method level doc.
        //
        // ~O(1) loops. It terminates randomly, based on input data. I have seen it as high as 18 for one input.
        // Usually it is less than 5.
        do {
            // O(n) shuffle
            Collections.shuffle(assignmentOrder, random);

            // O(n * m) validation
            final Optional<List<Assignment>> assignmentsOpt = validateAndConvertAssignments(assignmentOrder, input.exclusions());
            if (assignmentsOpt.isPresent()) {
                assignments = assignmentsOpt.get();
                break;
            }
        } while (true);

        return ImmutableAssignmentOutput.builder()
                .assignments(assignments)
                .build();
    }

    // O(n * m) algorithm, for n persons and m exclusions.
    private Optional<List<Assignment>> validateAndConvertAssignments(final List<String> assignmentOrder, final List<Exclusion> exclusions) {
        final int n = assignmentOrder.size();
        List<Assignment> assignments = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            final String giverId = assignmentOrder.get(i);
            final String receiverId = assignmentOrder.get((i + 1) % n);

            for (Exclusion exclusion : exclusions) {
                if (exclusion.equalsIgnoreOrder(giverId, receiverId)) {
                    return Optional.empty();
                }
            }

            assignments.add(ImmutableAssignment.builder()
                    .giverPersonId(giverId)
                    .receiverPersonId(receiverId)
                    .build());
        }

        return Optional.of(assignments);
    }
}
