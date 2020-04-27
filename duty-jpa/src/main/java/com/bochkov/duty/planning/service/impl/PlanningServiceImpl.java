package com.bochkov.duty.planning.service.impl;

import com.bochkov.duty.jpa.entity.DutyRoster;
import com.bochkov.duty.planning.service.PlanningService;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.stereotype.Service;

@Service
public class PlanningServiceImpl implements PlanningService {

    @Override
    public DutyRoster solve(DutyRoster dutyRoster) {
        SolverFactory<DutyRoster> solverFactory =
                SolverFactory.createFromXmlResource("com/bochkov/duty/planning/service/SolverConfig1.xml");
        Solver<DutyRoster> solver = solverFactory.buildSolver();
        DutyRoster solved = solver.solve(dutyRoster);
        return solved;
    }
}
