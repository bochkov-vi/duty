package com.bochkov.duty.planning.solve.listener;

import com.bochkov.duty.planning.model.PlanningDuty;
import com.bochkov.duty.planning.model.PlanningReport;
import org.optaplanner.core.impl.domain.variable.listener.VariableListenerAdapter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public abstract class DutyChainChangeListener extends VariableListenerAdapter<PlanningDuty> {
    public static PlanningDuty findNext(PlanningReport report, PlanningDuty duty) {
        return report.getDutyList().stream()
                .filter(d -> Objects.equals(d.getPerson(), duty.getPerson())
                        && Objects.equals(d.getDutyType(), duty.getDutyType())
                        && duty.isBefore(d))
                .min(Comparator.comparing(PlanningDuty::getDay))
                .orElse(null);
    }

    public static PlanningDuty findPrev(PlanningReport report, PlanningDuty duty) {
        return report.getDutyList().stream().
                filter(d -> Objects.equals(d.getPerson(), duty.getPerson())
                        && Objects.equals(d.getDutyType(), duty.getDutyType())
                        && duty.isAfter(d))
                .max(Comparator.comparing(PlanningDuty::getDay))
                .orElse(null);
    }

   /* public void calculatePrevDuty(ScoreDirector scoreDirector, PlanningDuty planningDuty) {
        PlanningReport report = (PlanningReport) scoreDirector.getWorkingSolution();
        PlanningDuty oldPrev = planningDuty.getPrev();
        PlanningDuty findedPrev = findPrev(report, planningDuty);
        if (!Objects.equals(findedPrev, oldPrev)) {
            if (oldPrev != null) {
                scoreDirector.beforeVariableChanged(oldPrev, "next");
                oldPrev.setNext(null);
                scoreDirector.afterVariableChanged(oldPrev, "next");
            }
            planningDuty.setPrev(findedPrev);
            planningDuty.setDaysToPrev(Optional.ofNullable(findedPrev).map(planningDuty::daysTo).orElse(null));
            Optional.ofNullable(findedPrev).ifPresent(prev -> planningDuty.setDaysToPrev(planningDuty.daysTo(prev)));
            scoreDirector.beforeVariableChanged(findedPrev, "next");
            findedPrev.setNext(planningDuty);
            scoreDirector.beforeVariableChanged(findedPrev, "next");
        }
    }*/

    public void calculateNextDuty(ScoreDirector scoreDirector, PlanningDuty planningDuty) {
        PlanningReport report = (PlanningReport) scoreDirector.getWorkingSolution();
        PlanningDuty findedNext = findNext(report, planningDuty);
        planningDuty.setDaysToNext(Optional.ofNullable(findedNext).map(planningDuty::daysTo).orElse(null));

    }


}
