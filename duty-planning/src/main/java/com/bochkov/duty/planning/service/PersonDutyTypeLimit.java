package com.bochkov.duty.planning.service;

import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Person;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PersonDutyTypeLimit implements Serializable {
    Person person;
    DutyType dutyType;
    Integer max;
    Integer min;

    public PersonDutyTypeLimit(Person person) {
        this.person = person;
    }

    public PersonDutyTypeLimit(Person person, DutyType dutyType) {
        this.person = person;
        this.dutyType = dutyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonDutyTypeLimit that = (PersonDutyTypeLimit) o;
        return Objects.equal(person, that.person) &&
                Objects.equal(dutyType, that.dutyType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(person, dutyType);
    }
}
