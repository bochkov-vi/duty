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
    Integer idEmployee;

    @Column(name = "DATE")
    LocalDate date;

    public ShiftPK() {
    }

    public ShiftPK(Integer idEmployee, LocalDate date) {
        this.idEmployee = idEmployee;
        this.date = date;
    }

    public ShiftPK(Employee employee, Day day) {
        this.idEmployee = employee.id;
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
        return Objects.equal(idEmployee, shiftPK.idEmployee) &&
                Objects.equal(date, shiftPK.date);
    }

    @Override
    public String toString() {
        return "DutyPK{" +
                "idPerson='" + idEmployee + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idEmployee, date);
    }
}
