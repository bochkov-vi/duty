package com.bochkov.duty.planning.service;

import com.bochkov.duty.jpa.entity.Person;
import com.google.common.base.Objects;
import lombok.Data;

import java.io.Serializable;

@Data
public class PersonDutyCount implements Serializable, Comparable<PersonDutyCount> {

    Person person;

    Integer count;

    public PersonDutyCount(Person person, Number count) {
        this.person = person;
        this.count = count.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonDutyCount that = (PersonDutyCount) o;
        return Objects.equal(getPerson(), that.getPerson());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPerson());
    }

    @Override
    public int compareTo(PersonDutyCount o) {
        return person.compareTo(o.person);
    }
}
