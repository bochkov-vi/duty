package com.bochkov.duty.planning.service;

import org.optaplanner.core.api.score.stream.bi.BiConstraintCollector;
import org.optaplanner.core.api.score.stream.uni.UniConstraintCollector;
import org.optaplanner.core.impl.score.stream.bi.DefaultBiConstraintCollector;
import org.optaplanner.core.impl.score.stream.uni.DefaultUniConstraintCollector;

import java.io.Serializable;

public class VarianceConstraintCollector {


    public static <N extends Number> UniConstraintCollector<N, VarianceData, Double> variance() {
        return new DefaultUniConstraintCollector<>(VarianceData::new, (data, n) -> {
            accumulate(data, n);
            return new Runnable() {
                @Override
                public void run() {
                    reverse(data, n);
                }
            };
        }, (data) -> result(data));
    }

    public static <N extends Number, A> BiConstraintCollector<A, N, VarianceData, Double> varianceBi() {
        return new DefaultBiConstraintCollector<A, N, VarianceData, Double>(VarianceData::new, (data, a, n) -> {
            accumulate(data, n);
            return new Runnable() {
                @Override
                public void run() {
                    reverse(data, n);
                }
            };
        }, (data) -> result(data));
    }

    public static void accumulate(VarianceData data, Number value) {
        double x = ((Number) value).doubleValue();
        // Incremental algorithm to calculate variance:
        // https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
        data.count++;
        double lowerDelta = x - data.mean;
        data.mean += lowerDelta / data.count;
        double higherDelta = x - data.mean;
        data.squaredSum += lowerDelta * higherDelta;
    }

    public static void reverse(VarianceData data, Number value) {
        double x = ((Number) value).doubleValue();

        double higherDelta = x - data.mean;
        //without this if statement mean becomes NaN, and never escapes from there
        data.mean = data.count == 1 ? 0 : data.mean * data.count / (data.count - 1.0) - x / (data.count - 1.0);
        double lowerDelta = x - data.mean;
        data.count--;
        data.squaredSum -= lowerDelta * higherDelta;
    }

    public static Double result(VarianceData data) {
        return Math.round(data.squaredSum / data.count * 1000.0) / 1000.0;
    }


    public static class VarianceData implements Serializable {

        protected int count = 0;

        protected double mean = 0;

        protected double squaredSum = 0;
    }
}
