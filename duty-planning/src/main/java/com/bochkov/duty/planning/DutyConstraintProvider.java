package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.planning.service.LoadBalanceCollector;
import com.bochkov.duty.planning.service.VarianceConstraintCollector;
import org.apache.commons.lang3.tuple.Pair;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.util.Objects;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;
import static org.optaplanner.core.api.score.stream.Joiners.*;

public class DutyConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                notInitDutyDay(factory),
                notNextDutyDay(factory),
                personDutyTypeOnly(factory),
                onlyOneDutyForPersonPerDay(factory),
                moreIntervalBetweenDuties(factory),
                moreIntervalBetweenWeekendDuties(factory)
                ,moreIntervalBetweenStrongDuties(factory)
//                , moreIntervalBetweenDutiesForAllType(factory)
//                , fairDistributionDutyByCount(factory)
//                , fairDistributionDutyByCountAndDutyType(factory)
//                , fairDistributionDutyByCountAndDayType(factory)
//                , fairDistributionDutyByCountAndWeek(factory)
//                , fairDistributionDutyByCountAndWeekend(factory)
                , fairDistributionDutyByTime(factory)
        };
    }

    private Constraint notInitDutyDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class).filter(da -> da.getPerson() == null)
                .penalize("нельзя дежурства без людей", HardMediumSoftScore.ONE_MEDIUM);
    }

    private Constraint notNextDutyDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .join(DutyAssigment.class,
                        equal(DutyAssigment::getPerson, DutyAssigment::getPerson),
                        equal(DutyAssigment::getDayIndex, da -> da.getDayIndex() - 1),
                        filtering((da1, da2) -> da1.isEndOnNextDay()))
                .penalize("нельзя дежурить после длинного дежурства", HardMediumSoftScore.ONE_HARD);
    }


    private Constraint personDutyTypeOnly(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(da -> Objects.nonNull(da.getPerson()))
                .filter(da -> da.getPerson().getDutyTypes() != null && !da.getPerson().getDutyTypes().contains(da.getDutyType()))
                .penalize("вид дежурства только предусмотренный для сотрудника", HardMediumSoftScore.ONE_HARD);
    }

    private Constraint onlyOneDutyForPersonPerDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(dutyAssigment -> Objects.nonNull(dutyAssigment.getPerson()))
                .join(DutyAssigment.class,
                        equal(DutyAssigment::getPerson, DutyAssigment::getPerson),
                        equal(DutyAssigment::getDay, DutyAssigment::getDay),
                        filtering((da1, da2) -> !da1.equals(da2)))
                .penalize("только 1 дежурство  на 1 человека в 1 день", HardMediumSoftScore.ONE_HARD);
    }

    private Constraint moreIntervalBetweenStrongDuties(ConstraintFactory factory) {
        return factory.from(DutyPlanOptions.class)
                .join(DutyAssigment.class,
                        filtering((dpo, d) -> Objects.nonNull(d.getPerson())),
                        filtering((dpo, d) -> d.isEndOnNextDay()))
                .join(DutyAssigment.class,
                        equal((dpo, d1) -> d1.getPerson(), DutyAssigment::getPerson),
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), DutyAssigment::getDayIndex),
                        filtering((dpo, d1, d2) -> d1.isEndOnNextDay() && d2.isEndOnNextDay()),
                        filtering((dpo, d1, d2) -> d1.getDayIndex() - d2.getDayIndex() <= dpo.minInterval)
                )
                .penalize("нельзя суточные дежурства надо пореже", HardMediumSoftScore.ONE_MEDIUM, (dpo, d1, d2) -> dpo.minInterval - (d1.getDayIndex() - d2.getDayIndex()));
    }

    private Constraint moreIntervalBetweenDuties(ConstraintFactory factory) {
        return factory.from(DutyPlanOptions.class)
                .join(DutyAssigment.class, filtering((dpo, d) -> Objects.nonNull(d.getPerson())))
                .join(DutyAssigment.class,
                        equal((dpo, d1) -> d1.getPerson(), DutyAssigment::getPerson),
                        equal((dpo, d1) -> d1.getDutyType(), DutyAssigment::getDutyType),
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), DutyAssigment::getDayIndex),
                        filtering((dpo, d1, d2) -> d1.getDayIndex() - d2.getDayIndex() <= dpo.minInterval)
                )
                .penalize("надо больше промежутки между дежурствами", HardMediumSoftScore.ONE_SOFT, (dpo, d1, d2) -> dpo.minInterval - (d1.getDayIndex() - d2.getDayIndex()));
    }

    private Constraint moreIntervalBetweenDutiesForAllType(ConstraintFactory factory) {
        return factory.from(DutyPlanOptions.class)
                .join(DutyAssigment.class, filtering((dpo, d) -> Objects.nonNull(d.getPerson())))
                .join(DutyAssigment.class,
                        equal((dpo, d1) -> d1.getPerson(), DutyAssigment::getPerson),
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), DutyAssigment::getDayIndex),
                        filtering((dpo, d1, d2) -> d1.getDayIndex() - d2.getDayIndex() <= dpo.minInterval)
                )
                .penalize("больше интервалов между любыми дежурствами", HardMediumSoftScore.ONE_SOFT, (dpo, d1, d2) -> dpo.minInterval - (d1.getDayIndex() - d2.getDayIndex()));
    }

    private Constraint moreIntervalBetweenWeekendDuties(ConstraintFactory factory) {
        return factory.from(DutyPlanOptions.class)
                .join(DutyAssigment.class,
                        filtering((dpo, d) -> Objects.nonNull(d.getPerson()) && d.isWeekend())
                )
                .join(DutyAssigment.class,
                        equal((dpo, d1) -> d1.getPerson(), DutyAssigment::getPerson),
//                        equal((dpo, d1) -> d1.getDutyType(), DutyAssigment::getDutyType),
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), DutyAssigment::getDayIndex),
                        filtering((dpo, d1, d2) -> d2.isWeekend() && d1.getDayIndex() - d2.getDayIndex() <= dpo.minInterval)
                )
                .penalize("нельзя дежурства в выходные за подряд", HardMediumSoftScore.ONE_SOFT, (dpo, d1, d2) -> dpo.minInterval - (d1.getDayIndex() - d2.getDayIndex()));
    }

    private Constraint fairDistributionDutyByCount(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(da -> Objects.nonNull(da.getPerson()))
                .groupBy(DutyAssigment::getPerson, count())
                .groupBy(LoadBalanceCollector.loadBalanceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств между людьми", HardMediumSoftScore.ONE_SOFT,
                        Long::intValue);

    }

    private Constraint fairDistributionDutyByCountAndDutyType(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(da -> Objects.nonNull(da.getPerson()))
                .groupBy(da -> Pair.of(da.getPerson(), da.getDutyType()), count())
                .groupBy(LoadBalanceCollector.loadBalanceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств у людей по видам дежурств", HardMediumSoftScore.ONE_SOFT,
                        Long::intValue);

    }

    private Constraint fairDistributionDutyByCountAndDayType(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(da -> Objects.nonNull(da.getPerson()))
                .groupBy(da -> Pair.of(da.getPerson(), da.getDay().getDayOfWeek()), count())
                .groupBy(LoadBalanceCollector.loadBalanceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств у людей по видам дней", HardMediumSoftScore.ONE_SOFT,
                        Long::intValue);

    }

    private Constraint fairDistributionDutyByCountAndWeek(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(da -> Objects.nonNull(da.getPerson()))
                .groupBy(da -> Pair.of(da.getPerson(), da.getWeekIndex()), count())
                .groupBy(LoadBalanceCollector.loadBalanceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств у людей по неделям", HardMediumSoftScore.ONE_SOFT,
                        Long::intValue);

    }

    private Constraint fairDistributionDutyByCountAndWeekend(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(da -> Objects.nonNull(da.getPerson()) && da.isWeekend())
                .groupBy(da -> da.getPerson(), count())
                .groupBy(LoadBalanceCollector.loadBalanceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств у людей по выходным", HardMediumSoftScore.ONE_SOFT,
                        Long::intValue);

    }

    private Constraint fairDistributionDutyByTime(ConstraintFactory factory) {
        return factory.from(Person.class)
                .join(factory.fromUnfiltered(DutyAssigment.class))
                //.filter(da -> Objects.nonNull(da.getPerson()))
                .groupBy((p, da) -> p, sum((p, da) -> {
                    int v = 0;
                    if (Objects.equals(p, da.getPerson())) {
                        v = (int) da.getOverTime().toHours();
                    }
                    // System.out.printf("%s:%s\n", p, v);
                    return v;
                }))
                .groupBy(VarianceConstraintCollector.varianceBi())
                .penalize("распределение дежурств у людей по длительности", HardMediumSoftScore.ONE_SOFT,
                        (v) -> {
//                            System.out.printf("balance:%s\n", v);
                            return (int) (Math.round(v)*1000);
                        });

    }
}
