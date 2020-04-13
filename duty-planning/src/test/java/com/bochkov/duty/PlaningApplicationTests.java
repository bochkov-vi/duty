package com.bochkov.duty;

import com.bochkov.duty.planning.PlaningSpringConfiguration;
import com.bochkov.duty.planning.model.PlanningReport;
import com.bochkov.duty.planning.service.PlanningReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PlaningSpringConfiguration.class)
public class PlaningApplicationTests {

    @Autowired
    PlanningReportService planningReportService;

    @Test
    public void testPlanning() {
        PlanningReport planningReport = planningReportService.loadReport(4);
        System.out.println(planningReport);
        SolverFactory<PlanningReport> solverFactory =SolverFactory.createFromXmlResource("com/bochkov/duty/planning/solve/config.xml");
        Solver<PlanningReport> solver = solverFactory.buildSolver();
        solver.solve(planningReport);
        PlanningReport solution = solver.getBestSolution();
        System.out.println(solution);
    }

    @Test
    public void testLoading() {
        PlanningReport planningReport = planningReportService.loadReport(4);
        System.out.println(planningReport);
    }

}
