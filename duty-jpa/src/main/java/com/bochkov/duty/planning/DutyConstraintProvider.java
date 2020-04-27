package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.EmployeeShiftTypeLimit;
import com.bochkov.duty.jpa.entity.ShiftAssignment;
import com.bochkov.duty.planning.service.DutyTypeInterval;
import com.bochkov.duty.planning.service.VarianceConstraintCollector;
import org.apache.commons.lang3.tuple.Pair;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.util.Objects;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.*;
import static org.optaplanner.core.api.score.stream.Joiners.*;

public class DutyConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                notInitDutyDay(factory),
                notNextDutyDay(factory),
                personDutyTypeOnly(factory),
                onlyOneDutyForPersonPerDay(factory)
                , moreIntervalBetweenDuties(factory)
                , moreIntervalBetweenWeekendDuties(factory)
                , moreIntervalBetweenStrongDuties(factory)
                , moreIntervalBetweenDutiesForAllType(factory)
                , fairDistributionDutyByCount(factory)
                , fairDistributionDutyByCountAndDutyType(factory)
                , fairDistributionDutyByCountAndDayType(factory)
                //  , fairDistributionDutyByCountAndWeek(factory)
                , fairDistributionDutyByCountAndWeekend(factory)
                , fairDistributionDutyByTime(factory)
                , personDutyTypeMaxCountLimit(factory)
                , personAllDutyMaxCountLimit(factory)
        };
    }

    private Constraint notInitDutyDay(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class).filter(da -> da.getEmployee() == null)
                .penalize("нельзя дежурства без людей", HardMediumSoftScore.ONE_MEDIUM);
    }

    private Constraint notNextDutyDay(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class)
                .join(ShiftAssignment.class,
                        equal(ShiftAssignment::getEmployee, ShiftAssignment::getEmployee),
                        equal(ShiftAssignment::getDayIndex, da -> da.getDayIndex() - 1),
                        filtering((da1, da2) -> da1.isEndOnNextDay()))
                .penalize("нельзя дежурить после длинного дежурства", HardMediumSoftScore.ONE_HARD);
    }


    private Constraint personDutyTypeOnly(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class)
                .filter(da -> Objects.nonNull(da.getEmployee()))
                .filter(da -> da.getEmployee().getShiftTypes() != null && !da.getEmployee().getShiftTypes().contains(da.getShiftType()))
                .penalize("вид дежурства только предусмотренный для сотрудника", HardMediumSoftScore.ONE_HARD);
    }

    private Constraint onlyOneDutyForPersonPerDay(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class)
                .filter(shiftAssignment -> Objects.nonNull(shiftAssignment.getEmployee()))
                .join(ShiftAssignment.class,
                        equal(ShiftAssignment::getEmployee, ShiftAssignment::getEmployee),
                        equal(ShiftAssignment::getDay, ShiftAssignment::getDay),
                        filtering((da1, da2) -> !da1.equals(da2)))
                .penalize("только 1 дежурство  на 1 человека в 1 день", HardMediumSoftScore.ONE_HARD);
    }

    private Constraint moreIntervalBetweenStrongDuties(ConstraintFactory factory) {
        return factory.from(DutyTypeInterval.class)
                .join(ShiftAssignment.class,
                        Joiners.equal(DutyTypeInterval::getShiftType, ShiftAssignment::getShiftType),
                        filtering((dpo, d) -> Objects.nonNull(d.getEmployee())),
                        filtering((dpo, d) -> d.isEndOnNextDay()))
                .join(ShiftAssignment.class,
                        equal((dpo, d1) -> d1.getEmployee(), ShiftAssignment::getEmployee),
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), ShiftAssignment::getDayIndex),
                        filtering((dpo, d1, d2) -> d1.isEndOnNextDay() && d2.isEndOnNextDay()),
                        filtering((dpo, d1, d2) -> d1.getDayIndex() - d2.getDayIndex() <= dpo.getMin())
                )
                .penalize("нельзя суточные дежурства надо пореже", HardMediumSoftScore.ONE_MEDIUM, (dpo, d1, d2) -> dpo.getMin() - (d1.getDayIndex() - d2.getDayIndex()));
    }

    private Constraint moreIntervalBetweenDuties(ConstraintFactory factory) {
        return factory.from(DutyTypeInterval.class)
                .join(ShiftAssignment.class,
                        equal(DutyTypeInterval::getShiftType, ShiftAssignment::getShiftType),
                        filtering((dpo, d) -> Objects.nonNull(d.getEmployee())))
                .join(ShiftAssignment.class,
                        equal((dti, d1) -> d1.getEmployee(), ShiftAssignment::getEmployee),
                        equal((dti, d1) -> dti.getShiftType(), ShiftAssignment::getShiftType),
                        greaterThanOrEqual((dti, d1) -> d1.getDayIndex(), ShiftAssignment::getDayIndex),
                        filtering((dti, d1, d2) -> d1.getDayIndex() - d2.getDayIndex() <= dti.getMin())
                )
                .penalize("надо больше промежутки между дежурствами", HardMediumSoftScore.ONE_SOFT, (dpo, d1, d2) -> dpo.getMin() - (d1.getDayIndex() - d2.getDayIndex()));
    }

    private Constraint moreIntervalBetweenDutiesForAllType(ConstraintFactory factory) {
        return factory.from(DutyTypeInterval.class)
                .join(ShiftAssignment.class,
                        equal(DutyTypeInterval::getShiftType, ShiftAssignment::getShiftType),
                        filtering((dpo, d) -> Objects.nonNull(d.getEmployee())))
                .join(ShiftAssignment.class,
                        equal((dti, d1) -> dti.getShiftType(), ShiftAssignment::getShiftType),
                        equal((dpo, d1) -> d1.getEmployee(), ShiftAssignment::getEmployee),
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), ShiftAssignment::getDayIndex),
                        filtering((dpo, d1, d2) -> d1.getDayIndex() - d2.getDayIndex() <= dpo.getMin())
                )
                .penalize("больше интервалов между любыми дежурствами", HardMediumSoftScore.ONE_SOFT, (dpo, d1, d2) -> dpo.getMin() - (d1.getDayIndex() - d2.getDayIndex()));
    }

    private Constraint moreIntervalBetweenWeekendDuties(ConstraintFactory factory) {
        return factory.from(DutyTypeInterval.class)
                .join(ShiftAssignment.class,
                        Joiners.equal(DutyTypeInterval::getShiftType, ShiftAssignment::getShiftType),
                        filtering((dpo, d) -> d.isWeekend()),
                        filtering((dpo, d) -> Objects.nonNull(d.getEmployee()))
                )
                .join(ShiftAssignment.class,
                        equal((dti, d1) -> dti.getShiftType(), ShiftAssignment::getShiftType),
                        equal((dpo, d1) -> d1.getEmployee(), ShiftAssignment::getEmployee),
//                        equal((dpo, d1) -> d1.getShiftType(), ShiftAssignment::getShiftType),
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), ShiftAssignment::getDayIndex),
                        filtering((dpo, d1, d2) -> d2.isWeekend()),
                        filtering((dpo, d1, d2) -> d1.getDayIndex() - d2.getDayIndex() <= dpo.getMin())
                )
                .penalize("нельзя дежурства в выходные за подряд", HardMediumSoftScore.ONE_SOFT, (dpo, d1, d2) -> dpo.getMin() - (d1.getDayIndex() - d2.getDayIndex()));
    }


    private Constraint fairDistributionDutyByCount(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class)
                .filter(da -> Objects.nonNull(da.getEmployee()))
                .groupBy(ShiftAssignment::getEmployee, count())
                .groupBy(VarianceConstraintCollector.varianceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств между людьми", HardMediumSoftScore.ONE_SOFT,
                        n -> n.intValue() * 1000);

    }

    private Constraint fairDistributionDutyByCountAndDutyType(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class)
                //.filter(da -> Objects.nonNull(da.getEmployee()))
                .groupBy(da -> Pair.of(da.getEmployee(), da.getShiftType()), count())
                .groupBy(VarianceConstraintCollector.varianceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств у людей по видам дежурств", HardMediumSoftScore.ONE_SOFT,
                        n -> n.intValue() * 1000);

    }

    private Constraint fairDistributionDutyByCountAndDayType(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class)
                .filter(da -> Objects.nonNull(da.getEmployee()))
                .groupBy(da -> Pair.of(da.getEmployee(), da.getDay().dayOfWeek()), count())
                .groupBy(VarianceConstraintCollector.varianceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств у людей по видам дней", HardMediumSoftScore.ONE_SOFT,
                        n -> n.intValue() * 1000);

    }

    private Constraint fairDistributionDutyByCountAndWeek(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class)
                .filter(da -> Objects.nonNull(da.getEmployee()))
                .groupBy(da -> Pair.of(da.getEmployee(), da.getWeekIndex()), count())
                .groupBy(VarianceConstraintCollector.varianceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств у людей по неделям", HardMediumSoftScore.ONE_SOFT,
                        n -> n.intValue() * 1000);

    }

    private Constraint fairDistributionDutyByCountAndWeekend(ConstraintFactory factory) {
        return factory.from(ShiftAssignment.class)
                .filter(da -> Objects.nonNull(da.getEmployee()) && da.isWeekend())
                .groupBy(ShiftAssignment::getEmployee, count())
                .groupBy(VarianceConstraintCollector.varianceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств у людей по выходным", HardMediumSoftScore.ONE_SOFT,
                        n -> n.intValue() * 1000);

    }

    private Constraint fairDistributionDutyByTime(ConstraintFactory factory) {
        return factory.from(Employee.class)
                .join(factory.fromUnfiltered(ShiftAssignment.class))
                //.filter(da -> Objects.nonNull(da.getEmployee()))
                .groupBy((p, da) -> p, sum((p, da) -> {
                    int v = 0;
                    if (Objects.equals(p, da.getEmployee())) {
                        v = (int) da.getOverTime().toHours();
                    }
                    // System.out.printf("%s:%s\n", p, v);
                    return v;
                }))
                .groupBy(VarianceConstraintCollector.varianceBi())
                .penalize("распределение дежурств у людей по длительности", HardMediumSoftScore.ONE_SOFT,
                        (v) -> {
//                            System.out.printf("balance:%s\n", v);
                            return (int) (Math.round(v) * 1000);
                        });

    }

    private Constraint personDutyTypeMaxCountLimit(ConstraintFactory factory) {
        return factory.from(EmployeeShiftTypeLimit.class)
                .filter(lm -> Objects.nonNull(lm.getMax()))
                .join(ShiftAssignment.class,
                        Joiners.equal(EmployeeShiftTypeLimit::getEmployee, ShiftAssignment::getEmployee),
                        Joiners.equal(EmployeeShiftTypeLimit::getShiftType, ShiftAssignment::getShiftType)
                )
                .groupBy((lm, da) -> lm, countBi())
                .filter((lm, cnt) -> lm.getMax() < cnt)
                .penalize("максимальный предел количества определенного типа дежурств", HardMediumSoftScore.ONE_HARD, (lm, cnt) -> cnt - lm.getMax());
    }

    private Constraint personAllDutyMaxCountLimit(ConstraintFactory factory) {
        return factory.from(EmployeeShiftTypeLimit.class)
                .filter(lm -> Objects.nonNull(lm.getMax()))
                .filter(lm -> Objects.isNull(lm.getShiftType()))
                .join(ShiftAssignment.class,
                        Joiners.equal(EmployeeShiftTypeLimit::getEmployee, ShiftAssignment::getEmployee)
                )
                .groupBy((lm, da) -> lm, countBi())
                .filter((lm, cnt) -> lm.getMax() < cnt)
                .penalize("максимальный предел количества дежурств", HardMediumSoftScore.ONE_HARD, (lm, cnt) -> cnt - lm.getMax());
    }

}
