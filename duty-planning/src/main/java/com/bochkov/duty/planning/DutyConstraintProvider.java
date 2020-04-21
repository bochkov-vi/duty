package com.bochkov.duty.planning;

import com.bochkov.duty.planning.service.VarianceCollector;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.util.Objects;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
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
                moreIntervalBetweenWeekendDuties(factory),
                fairDistributionDutyByCount(factory)
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


    private Constraint moreIntervalBetweenDuties(ConstraintFactory factory) {
        return factory.from(DutyPlanOptions.class)
                .join(DutyAssigment.class, filtering((dpo, d) -> Objects.nonNull(d.getPerson())))
                .join(DutyAssigment.class,
                        equal((dpo, d1) -> d1.getPerson(), DutyAssigment::getPerson),
                        equal((dpo, d1) -> d1.getDutyType(), DutyAssigment::getDutyType),
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), DutyAssigment::getDayIndex),
                        filtering((dpo, d1, d2) -> d1.getDayIndex() - d2.getDayIndex() <= dpo.minInterval)
                )
                .penalize("нельзя дежурства за подряд", HardMediumSoftScore.ONE_MEDIUM, (dpo, d1, d2) -> dpo.minInterval - (d1.getDayIndex() - d2.getDayIndex()));
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
                        greaterThanOrEqual((dpo, d1) -> d1.getDayIndex(), DutyAssigment::getDayIndex),
                        filtering((dpo, d1, d2) -> d2.isWeekend() && d1.getDayIndex() - d2.getDayIndex() <= dpo.minInterval)
                )
                .penalize("нельзя дежурства в выходные за подряд", HardMediumSoftScore.ONE_SOFT, (dpo, d1, d2) -> dpo.minInterval - (d1.getDayIndex() - d2.getDayIndex()));
    }

    private Constraint fairDistributionDutyByCount(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(da -> Objects.nonNull(da.getPerson()))
                .groupBy(DutyAssigment::getPerson, count())
                .groupBy(VarianceCollector.varianceBi())
                /* .groupBy(StatisticCollectors.varianceBi())*/
                .penalize("распределение дежурств между людьми", HardMediumSoftScore.ONE_SOFT,
                        (a) -> (int) Math.floor(a * 1000.0));

    }

}
