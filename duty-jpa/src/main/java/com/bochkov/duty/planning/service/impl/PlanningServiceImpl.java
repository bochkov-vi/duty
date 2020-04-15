package com.bochkov.duty.planning.service.impl;

import com.bochkov.duty.planning.DutyPlan;
import com.bochkov.duty.planning.service.PlanningService;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.stereotype.Service;

@Service
public class PlanningServiceImpl implements PlanningService {

    @Override
    public DutyPlan solve(DutyPlan dutyPlan) {
        SolverFactory<DutyPlan> solverFactory =
                SolverFactory.createFromXmlResource("com/bochkov/duty/planning/service/SolverConfig1.xml");
        Solver<DutyPlan> solver = solverFactory.buildSolver();
        DutyPlan solved = solver.solve(dutyPlan);
        return solved;
    }
}
