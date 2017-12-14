package com.antigenomics.alisa.estimator.mc;

import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloUtils {
    public static int nextInt(int exclusiveBound) {
        return ThreadLocalRandom.current().nextInt(exclusiveBound);
    }

    public static int[] nextIntArray(int exclusiveBount, int length) {
        return ThreadLocalRandom.current().ints(length, 0, exclusiveBount).toArray();
    }

    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
