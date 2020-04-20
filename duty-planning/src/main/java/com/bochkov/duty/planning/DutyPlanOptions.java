package com.bochkov.duty.planning;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class DutyPlanOptions implements Serializable {

    LocalDate start;

    LocalDate end;

    LocalDate windowStart;

    LocalDate windowEnd;

    Integer minInterval = 4;

}
