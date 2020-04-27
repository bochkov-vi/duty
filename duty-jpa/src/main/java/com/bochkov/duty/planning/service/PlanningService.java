package com.bochkov.duty.planning.service;

import com.bochkov.duty.jpa.entity.DutyRoster;

public interface PlanningService {
    DutyRoster solve(DutyRoster dutyRoster);
}
