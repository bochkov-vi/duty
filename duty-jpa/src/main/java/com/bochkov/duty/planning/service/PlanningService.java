package com.bochkov.duty.planning.service;

import com.bochkov.duty.planning.DutyPlan;

public interface PlanningService {
    DutyPlan solve(DutyPlan dutyPlan);
}
