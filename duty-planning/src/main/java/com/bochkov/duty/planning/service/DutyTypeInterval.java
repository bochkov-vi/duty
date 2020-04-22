package com.bochkov.duty.planning.service;

import com.bochkov.duty.jpa.entity.DutyType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DutyTypeInterval implements Serializable {
    DutyType dutyType;
    Integer min;

    public DutyTypeInterval(DutyType dutyType, Integer min) {
        this.dutyType = dutyType;
        this.min = min;
    }
}
