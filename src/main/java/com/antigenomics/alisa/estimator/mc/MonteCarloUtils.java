package com.antigenomics.alisa.estimator.mc;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloUtils {
    public static int nextInt(int exclusiveBound) {
        return ThreadLocalRandom.current().nextInt(exclusiveBound);
    }

    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
