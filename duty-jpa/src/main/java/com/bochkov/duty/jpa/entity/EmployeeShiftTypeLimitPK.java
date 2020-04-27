package com.bochkov.duty.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EmployeeShiftTypeLimitPK implements Serializable {

    @Column(name = "ID_DUTY_ROSTER")
    Integer idDutyRoster;

    @Column(name = "ID_EMPLOYEE")
    Integer idEmployee;

    @Column(name = "ID_SHIFT_TYPE")
    Integer idShiftType;

}
