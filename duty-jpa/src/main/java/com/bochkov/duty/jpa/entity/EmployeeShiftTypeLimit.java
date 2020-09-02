package com.bochkov.duty.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "EMPLOYEE_SHIFT_TYPE_LIMIT")
public class EmployeeShiftTypeLimit extends AbstractEntity<Long> {

    @Id
    @Column(name = "ID_EMPLOYEE_SHIFT_TYPE_LIMIT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEE_SHIFT_TYPE_LIMIT_SEQ")
    @SequenceGenerator(name = "EMPLOYEE_SHIFT_TYPE_LIMIT_SEQ", allocationSize = 1)
    @EqualsAndHashCode.Include
    Long id;

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

    @Column(name = "DAYS_COUNT")
    Integer daysCount;

    public EmployeeShiftTypeLimit(Employee employee) {
        this.employee = employee;
    }

    public EmployeeShiftTypeLimit(Employee employee, ShiftType shiftType) {
        this.employee = employee;
        this.shiftType = shiftType;
    }
}
