package com.bochkov.duty.planning.service;

import lombok.Data;
import org.optaplanner.core.api.score.stream.bi.BiConstraintCollector;
import org.optaplanner.core.impl.score.stream.bi.DefaultBiConstraintCollector;

import java.io.Serializable;

public class VarianceCollector {

    public static <A, B extends Number> BiConstraintCollector<A, B, VarianceData, Double> varianceBi() {
        BiConstraintCollector<A, B, VarianceData, Double> result =
                new DefaultBiConstraintCollector<A, B, VarianceData, Double>(
                        VarianceData::new,
                        (varianceData, argA, number) -> {
                            accumulate(varianceData, number);
                            return (Runnable) () -> reverse(varianceData, number);
                        },
                        data -> data.squaredSum / data.count
                );
        return result;
    }

    public static void accumulate(VarianceData data, Object value) {
        double x = ((Number) value).doubleValue();
        System.out.println(String.format("accumulate %s\t%s", value, data));
        data.count++;
        double lowerDelta = x - data.mean;
        data.mean += lowerDelta / data.count;
        double higherDelta = x - data.mean;
        data.squaredSum += lowerDelta * higherDelta;
    }

    public static void reverse(VarianceData data, Object value) {
        System.out.println(String.format("reverse %s\t%s", value, data));

        double x = ((Number) value).doubleValue();
        double higherDelta = x - data.mean;
        data.mean = data.count == 1 ? 0 : data.mean * data.count / (data.count - 1.0) - x / (data.count - 1.0);
        double lowerDelta = x - data.mean;
        data.count--;
        data.squaredSum -= lowerDelta * higherDelta;
    }

    @Data
    public static class VarianceData implements Serializable {
        protected int count = 0;
        protected double mean = 0;
        protected double squaredSum = 0;

    }

}
