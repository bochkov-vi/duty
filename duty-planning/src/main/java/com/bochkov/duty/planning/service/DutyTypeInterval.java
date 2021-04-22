package com.bochkov.duty.planning.service;

import com.bochkov.duty.jpa.entity.ShiftType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DutyTypeInterval implements Serializable {
    ShiftType shiftType;
    Integer min;

    public DutyTypeInterval(ShiftType shiftType, Integer min) {
        this.shiftType = shiftType;
        this.min = min;
    }
}
