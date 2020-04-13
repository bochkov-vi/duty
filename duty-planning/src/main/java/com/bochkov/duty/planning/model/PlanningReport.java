package com.bochkov.duty.planning.model;

import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.entity.UiOptions;
import lombok.experimental.Accessors;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@PlanningSolution
@NoArgsConstructor
@RequiredArgsConstructor
public class PlanningReport {
    @NonNull
    private Report report;

    @NonNull
    @ProblemFactCollectionProperty
    private List<PlanningPerson> personList;

    @NonNull
    @PlanningEntityCollectionProperty
    private List<PlanningDuty> dutyList;

    @NonNull
    @ProblemFactCollectionProperty
    private List<PlanningDay> dayList;

    @NonNull
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "dutyType")
    private List<PlanningDutyType> dutyTypeList;

    @PlanningScore
    private HardMediumSoftScore score;


    @Override
    public String toString() {
        System.out.print("\t\t\t\t\t");
        dayList.forEach(d -> System.out.printf("\t%s", d.getDate().format(DateTimeFormatter.ofPattern("dd"))));
        System.out.println();
        System.out.print("\t\t\t\t\t");
        dayList.forEach(d -> System.out.printf("\t%s", d.getDate().format(DateTimeFormatter.ofPattern("EE"))));
        System.out.println();
        System.out.println("------------------------------");

        for (PlanningPerson person : personList) {
            System.out.printf("%.18s\t",person.toString());
            dayList.forEach(d -> System.out.printf("\t%s", dutyList.stream().filter(du-> Objects.equals(du.getPerson(),person) && Objects.equals(du.getDay(),d))
                    .map(du-> Optional.ofNullable(du.getDutyType()).map(PlanningDutyType::getDutyType).map(DutyType::getUiOptions).map(UiOptions::getPlainText).orElse("*"))
                    .findFirst().orElse("*")));
            System.out.println();
        }
        return super.toString();
    }
}
