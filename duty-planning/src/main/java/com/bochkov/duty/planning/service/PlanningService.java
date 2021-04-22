package com.bochkov.duty.planning.service;


import com.bochkov.duty.planning.domain.ShiftRostering;

public interface PlanningService {
    ShiftRostering solve(ShiftRostering shiftRostering);
}
