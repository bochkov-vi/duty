package com.bochkov.duty.planning.solve.listener;

import com.bochkov.duty.planning.model.PlanningDuty;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class DutyTypeChangeListener extends DutyChainChangeListener {

    @Override
    public void afterVariableChanged(ScoreDirector scoreDirector, PlanningDuty planningDuty) {
        super.afterVariableChanged(scoreDirector, planningDuty);
        calculateNextDuty(scoreDirector, planningDuty);
    }
}
