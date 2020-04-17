package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Person;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.io.Serializable;
import java.util.List;

@PlanningSolution
@Getter
@Setter
@Accessors(chain = true)
public class DutyPlan implements Serializable {

    @ProblemFactProperty
    DutyPlanOptions dutyPlanOptions;

    @ProblemFactCollectionProperty
    List<DutyType> dutyTypes;

    @ProblemFactCollectionProperty
    List<Day> days;

    @ValueRangeProvider(id = "persons")
    @ProblemFactCollectionProperty
    List<Person> persons;

    @PlanningEntityCollectionProperty
    List<DutyAssigment> duties;

    @PlanningScore
    private HardMediumSoftScore score;

}
