package com.bochkov.duty.jpa.entity;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@Embeddable
public class DutyPK implements Serializable {

    @Column(name = "id_person", length = 15)
    String idPerson;

    @Column(name = "date")
    LocalDate date;

    public DutyPK() {
    }

    public DutyPK(String idPerson, LocalDate date) {
        this.idPerson = idPerson;
        this.date = date;
    }

    public DutyPK(Person person, Day day) {
        this.idPerson = person.id;
        this.date = day.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DutyPK)) {
            return false;
        }
        DutyPK dutyPK = (DutyPK) o;
        return Objects.equal(idPerson, dutyPK.idPerson) &&
                Objects.equal(date, dutyPK.date);
    }

    @Override
    public String toString() {
        return "DutyPK{" +
                "idPerson='" + idPerson + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPerson, date);
    }
}
