package com.bochkov.duty.planning.service;

import com.bochkov.duty.jpa.entity.ShiftRostering;

public interface PlanningService {
    ShiftRostering solve(ShiftRostering shiftRostering);
}
