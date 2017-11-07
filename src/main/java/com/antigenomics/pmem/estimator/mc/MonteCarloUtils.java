package com.antigenomics.pmem.estimator.mc;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

public class MonteCarloUtils {
    public static final RandomGenerator MC_RND = new MersenneTwister(42);
}
