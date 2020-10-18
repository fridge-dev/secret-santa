package com.frj.secretsanta.app.api;

import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Value.Immutable
public interface SecretSantaBroadcastInput {
    String messageFormat();
    Set<String> personIdsToMessage();
    List<PersonData> people();
    List<Exclusion> exclusions();
    long rngSeed();

    @Value.Derived
    default List<String> allPersonIds() {
        final List<String> personIds = new ArrayList<>();
        for (PersonData person : people()) {
            personIds.add(person.personId());
        }

        return personIds;
    }

    @Value.Derived
    default Map<String, PersonData> peopleByPersonId() {
        Map<String, PersonData> map = new HashMap<>(people().size());
        for (PersonData person : people()) {
            map.put(person.personId(), person);
        }

        return map;
    }

    @Value.Derived
    default Map<String, String> phoneNumberByPersonId() {
        Map<String, String> map = new HashMap<>(people().size());
        for (PersonData person : people()) {
            map.put(person.personId(), person.phoneNumber());
        }

        return map;
    }
}
