package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.planning.service.DutyTypeInterval;
import com.bochkov.duty.planning.service.PersonDutyTypeLimit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PlanningSolution
@Getter
@Setter
@Accessors(chain = true)
public class DutyPlan implements Serializable {

    @ProblemFactProperty
    DutyPlanOptions dutyPlanOptions = new DutyPlanOptions();

    @ProblemFactCollectionProperty
    List<DutyType> dutyTypes;

    @ProblemFactCollectionProperty
    List<PersonDutyTypeLimit> personDutyTypeLimits;

    @ProblemFactCollectionProperty
    List<Day> days;


    @ProblemFactCollectionProperty
    List<Person> persons;

    @PlanningEntityCollectionProperty
    List<DutyAssigment> duties;

    @ProblemFactProperty()
    Integer minInterval;

    @PlanningScore
    private HardMediumSoftScore score;

    @ProblemFactCollectionProperty
    List<DutyTypeInterval> getDutyTypeIntervalList() {
        Map<DutyType, Long> map = persons.stream().flatMap(person -> person.getDutyTypes().stream().map(dt -> Pair.of(person, dt)))
                .collect(Collectors.groupingBy(Pair::getValue, Collectors.counting()));
        return map.entrySet().stream().map(e -> new DutyTypeInterval(e.getKey(), e.getValue().intValue() / 2 )).collect(Collectors.toList());
    }
}
