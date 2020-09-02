package com.bochkov.duty.planning.service.impl;

import com.bochkov.duty.planning.domain.ShiftRostering;
import com.bochkov.duty.planning.service.PlanningService;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.stereotype.Service;

@Service
public class PlanningServiceImpl implements PlanningService {

    @Override
    public ShiftRostering solve(ShiftRostering shiftRostering) {
        SolverFactory<ShiftRostering> solverFactory =
                SolverFactory.createFromXmlResource("com/bochkov/duty/planning/service/SolverConfig1.xml");
        Solver<ShiftRostering> solver = solverFactory.buildSolver();
        ShiftRostering solved = solver.solve(shiftRostering);
        return solved;
    }
}
