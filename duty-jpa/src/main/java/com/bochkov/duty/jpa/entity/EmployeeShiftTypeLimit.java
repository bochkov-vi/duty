package com.bochkov.duty.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "EMPLOYEE_DUTY_TYPE_LIMIT")
public class EmployeeShiftTypeLimit extends AbstractEntity<EmployeeShiftTypeLimitPK> {

    @EmbeddedId
    EmployeeShiftTypeLimitPK id;

    @ManyToOne
    @JoinColumn(name = "ID_DUTY_ROSTER", insertable = false, updatable = false)
    DutyRoster dutyRoster;

    @ManyToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE", updatable = false, insertable = false)
    Employee employee;

    @ManyToOne
    @JoinColumn(name = "ID_SHIFT_TYPE", referencedColumnName = "ID_SHIFT_TYPE", updatable = false, insertable = false)
    ShiftType shiftType;

    @Column(name = "MIN")
    Integer max;

    @Column(name = "MAX")
    Integer min;

    public EmployeeShiftTypeLimit(Employee employee) {
        this.employee = employee;
    }

    public EmployeeShiftTypeLimit(Employee employee, ShiftType shiftType) {
        this.employee = employee;
        this.shiftType = shiftType;
    }
}
