package com.bochkov.duty.planning.model;

import com.bochkov.duty.jpa.entity.Person;
import com.google.common.base.Objects;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@RequiredArgsConstructor

public class PlanningPerson {
    @NonNull
    private Person person;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningPerson)) {
            return false;
        }
        PlanningPerson that = (PlanningPerson) o;
        return Objects.equal(person, that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(person);
    }

    @Override
    public String toString() {
        return person.toString();
    }

    public String getId() {
        return person.getId();
    }
}
