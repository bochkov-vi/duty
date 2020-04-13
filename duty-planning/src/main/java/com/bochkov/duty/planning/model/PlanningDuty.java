package com.bochkov.duty.planning.model;

import com.bochkov.duty.jpa.entity.Duty;
import com.bochkov.duty.planning.solve.DutyDifficultyComparator;
import com.bochkov.duty.planning.solve.DutySelectionFilter;
import com.bochkov.duty.planning.solve.listener.DutyTypeChangeListener;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.*;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

@PlanningEntity(difficultyComparatorClass = DutyDifficultyComparator.class, movableEntitySelectionFilter = DutySelectionFilter.class)
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class PlanningDuty {
    @NonNull
    private Duty duty;

    @NonNull
    private PlanningPerson person;

    @NonNull
    private PlanningDay day;



    @CustomShadowVariable(sources = {@PlanningVariableReference(variableName = "dutyType")}, variableListenerClass = DutyTypeChangeListener.class)
    private Integer daysToNext;


    @PlanningVariable(valueRangeProviderRefs = "dutyType", nullable = true)
    private PlanningDutyType dutyType;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningDuty)) {
            return false;
        }
        PlanningDuty that = (PlanningDuty) o;
        return Objects.equal(duty, that.duty);
    }

    public boolean isAfter(PlanningDuty other) {
        return this.day.isAfter(other.getDay());
    }

    public boolean isBefore(PlanningDuty other) {
        return this.day.isBefore(other.getDay());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(duty);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("person", person)
                .add("day", day)
                .add("dutyType", dutyType)
                .toString();
    }

    public int daysTo(PlanningDuty duty) {
        return this.day.daysTo(duty.day);
    }
}
