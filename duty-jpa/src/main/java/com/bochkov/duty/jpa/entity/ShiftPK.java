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
public class ShiftPK implements Serializable {

    @Column(name = "ID_EMPLOYEE", length = 15)
    String idPerson;

    @Column(name = "DATE")
    LocalDate date;

    public ShiftPK() {
    }

    public ShiftPK(String idPerson, LocalDate date) {
        this.idPerson = idPerson;
        this.date = date;
    }

    public ShiftPK(Employee employee, Day day) {
        this.idPerson = employee.id;
        this.date = day.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShiftPK)) {
            return false;
        }
        ShiftPK shiftPK = (ShiftPK) o;
        return Objects.equal(idPerson, shiftPK.idPerson) &&
                Objects.equal(date, shiftPK.date);
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
