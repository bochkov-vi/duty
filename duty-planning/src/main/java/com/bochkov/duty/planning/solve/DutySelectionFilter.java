package com.bochkov.duty.planning.solve;

import com.bochkov.duty.planning.model.PlanningDuty;
import com.bochkov.duty.planning.model.PlanningReport;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class DutySelectionFilter implements SelectionFilter<PlanningReport, PlanningDuty> {
    @Override
    public boolean accept(ScoreDirector<PlanningReport> scoreDirector, PlanningDuty selection) {
        return scoreDirector.getWorkingSolution().getDutyTypeList().contains(selection.getDutyType());
    }
}
