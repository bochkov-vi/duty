package com.bochkov.duty.planning.service;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.optaplanner.core.api.score.stream.bi.BiConstraintCollector;
import org.optaplanner.core.api.score.stream.uni.UniConstraintCollector;
import org.optaplanner.core.impl.score.stream.bi.DefaultBiConstraintCollector;
import org.optaplanner.core.impl.score.stream.uni.DefaultUniConstraintCollector;

public class StatisticCollectors {

    public static UniConstraintCollector<?, SummaryStatistics, SummaryStatistics> summaryStatistics() {
        UniConstraintCollector<?, SummaryStatistics, SummaryStatistics> result =
                new DefaultUniConstraintCollector<>(
                        SummaryStatistics::new,
                        (ss, data) -> (Runnable) () -> ss.addValue(((Number) data).doubleValue()), (ss) -> ss
                );
        return result;
    }

    public static <A, B extends Number> BiConstraintCollector<A, B, SummaryStatistics, SummaryStatistics> summaryStatisticsBi() {
        BiConstraintCollector<A, B, SummaryStatistics, SummaryStatistics> result =
                new DefaultBiConstraintCollector<>(
                        SummaryStatistics::new,
                        (ss, data1, number) -> (Runnable) () -> ss.addValue(number.doubleValue()), (ss) -> ss
                );
        return result;
    }

    public static <A, B extends Number> BiConstraintCollector<A, B, SummaryStatistics, Double> varianceBi() {
        BiConstraintCollector<A, B, SummaryStatistics, Double> result =
                new DefaultBiConstraintCollector<>(
                        SummaryStatistics::new,
                        (ss, data1, number) -> (Runnable) () -> ss.addValue(number.doubleValue()),
                        ss->ss.getVariance()
                );
        return result;
    }


}
