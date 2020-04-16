package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Person;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.uni.UniConstraintCollector;
import org.optaplanner.core.impl.score.stream.uni.DefaultUniConstraintCollector;

import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;

public class DutyConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{notInitDutyDay(factory),
                notNextDutyDay(factory),
                leastWeekend(factory),
                dispersionOfDutyCount(factory),
                moreIntervalBeteenDuties(factory),
                dispersionOfDutyCount2(factory)};
    }

    private Constraint notInitDutyDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class).filter(da -> da.person == null)
                .penalize("нельзя дежурства без людей", HardMediumSoftScore.ONE_MEDIUM);
    }

    private Constraint notNextDutyDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .join(DutyAssigment.class)
                .filter((d1, d2) -> Objects.equals(d1.getPerson(), d2.getPerson()) && d1.day.getId().equals(d2.getDay().getId().plusDays(1)))
                .penalize("нельзя одно и тоже дежурство за подряд", HardMediumSoftScore.ONE_HARD);
    }

    private Constraint leastWeekend(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(DutyAssigment::isWeekend)
                .groupBy(DutyAssigment::getPerson, count())
                .filter((p, cnt) -> cnt > 1)
                .penalize("меньше выходных дежурств", HardMediumSoftScore.ONE_SOFT, (person, cnt) -> cnt * 2);
    }

    private Constraint dispersionOfDutyCount(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .groupBy(count())
                .groupBy(variance())
                .penalize("Распределение дежурств по людям должно быть равномерным", HardMediumSoftScore.ONE_SOFT, new ToIntFunction<Double>() {
                    @Override
                    public int applyAsInt(Double v) {
                        return new Double(v * 10).intValue();
                    }
                });
    }

    private Constraint dispersionOfDutyCount2(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .join(Person.class)
                .filter((d, p) -> Objects.equals(d.getPerson(), p))
                .groupBy((d, p) -> d.getDay().getId().get(WeekFields.of(Locale.getDefault()).weekOfYear()))
                .groupBy(count())
                .groupBy(variance())
                .penalize("Распределение дежурств по неделям должно быть равномерным", HardMediumSoftScore.ONE_SOFT, new ToIntFunction<Double>() {
                    @Override
                    public int applyAsInt(Double v) {
                        return new Double(v * 10).intValue();
                    }
                });
    }

    private Constraint moreIntervalBeteenDuties(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .join(Person.class)
                .filter((d, p) -> Objects.equals(d.getPerson(), p))
                .join(DutyAssigment.class)
                .filter((d1, p, d2) -> Objects.equals(d1.getPerson(), d2.getPerson()))
                .filter((d1, p, d2) -> d1.day.isBefore(d2.day))
                .filter((d1, p, d2) -> (int) ChronoUnit.DAYS.between(d1.day.getId(), d2.day.getId()) < 7)
                .penalize("больше интервал между дежурствами", HardMediumSoftScore.ONE_SOFT,
                        (d1, p, d2) -> 7 - (int) ChronoUnit.DAYS.between(d1.day.getId(), d2.day.getId()));
    }

    public UniConstraintCollector<Integer, SummaryStatistics, Double> variance() {
        UniConstraintCollector<Integer, SummaryStatistics, Double> result = null;
        result = new DefaultUniConstraintCollector<>(SynchronizedSummaryStatistics::new,
                new BiFunction<SummaryStatistics, Integer, Runnable>() {
                    @Override
                    public Runnable apply(SummaryStatistics ss, Integer a) {
                        return new Runnable() {
                            @Override
                            public void run() {
                                ss.addValue(a);
                            }
                        };
                    }
                },
                SummaryStatistics::getVariance);
        return result;
    }
}
