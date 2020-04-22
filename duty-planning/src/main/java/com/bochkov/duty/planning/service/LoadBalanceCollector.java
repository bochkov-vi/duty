package com.bochkov.duty.planning.service;

import lombok.Data;
import org.optaplanner.core.api.score.stream.bi.BiConstraintCollector;
import org.optaplanner.core.impl.score.stream.bi.DefaultBiConstraintCollector;

import java.io.Serializable;

public class LoadBalanceCollector {


    public static <A, B extends Number> BiConstraintCollector<A, B, LoadBalanceData, Long> loadBalanceBi() {
        BiConstraintCollector<A, B, LoadBalanceData, Long> result =
                new DefaultBiConstraintCollector<A, B, LoadBalanceData, Long>(
                        LoadBalanceData::new,
                        (loadBalanceData, argA, number) -> {
//                            System.out.printf("%s:%s\n", argA, number);
                            accumulate(loadBalanceData, number);
                            return (Runnable) () -> {
                                reverse(loadBalanceData, number);
                            };
                        },
                        LoadBalanceData::getMeanDeviationSquaredSumRootMillis
                );
        return result;
    }

    public static void accumulate(LoadBalanceData data, Number o) {
//        System.out.println(String.format("before accumulate %s\t%s", o, data));
        long value = o.longValue();
        data.n++;
        data.sum += value;
        data.squaredSum += value * value;
//        System.out.println(String.format("after accumulate %s\t%s", value, data));
    }

    public static void reverse(LoadBalanceData data, Number o) {
//        System.out.println(String.format("brfore reverse %s\t%s", o, data));
        long value = o.byteValue();
        data.n--;
        data.sum -= value;
        data.squaredSum -= value * value;
//        System.out.println(String.format("after reverse %s\t%s", value, data));

    }

    @Data
    public static class LoadBalanceData implements Serializable {
        private long n;
        private long sum;
        // the sum of squared deviation from zero
        private long squaredSum;

        public long getMeanDeviationSquaredSumRootMillis() {
            return getMeanDeviationSquaredSumRoot(1_000.0);
        }

        public long getMeanDeviationSquaredSumRootMicros() {
            return getMeanDeviationSquaredSumRoot(1_000_000.0);
        }

        /**
         * Like standard deviation, but doesn't divide by n.
         *
         * @param scaleMultiplier {@code > 0}
         * @return {@code >= 0}, {@code latexmath:[f(n) = \sqrt{\sum_{i=1}^{n} (x_i - \overline{x})^2}]} multiplied by scaleMultiplier
         */
        public long getMeanDeviationSquaredSumRoot(double scaleMultiplier) {
            // quicklatex.com: f(n) = \sqrt{\sum_{i=1}^{n} (x_i - \overline{x})^2} = \sqrt{\sum_{i=1}^{n} x_i^2 - \frac{(\sum_{i=1}^{n} x_i)^2}{n}}
            double meanDeviationSquaredSum = (double) squaredSum - ((double) (sum * sum) / n);
            return (long) (Math.sqrt(meanDeviationSquaredSum) * scaleMultiplier);
        }
    }

}
