package com.bochkov.duty.planning.solve;

import com.bochkov.duty.planning.model.PlanningDuty;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

public class DutyDifficultyComparator implements Comparator<PlanningDuty> {
    @Override
    public int compare(PlanningDuty a, PlanningDuty b) {
        return new CompareToBuilder()
                .append(a.getDay().getDayType().getDayCountToWeekend(), b.getDay().getDayType().getDayCountToWeekend())
                .append(a.getDay().getIndex(), b.getDay().getIndex())
                .toComparison();
    }
}
