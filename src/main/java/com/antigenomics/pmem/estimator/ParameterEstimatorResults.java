package com.antigenomics.pmem.estimator;

import com.antigenomics.pmem.representation.Representation;

public final class ParameterEstimatorResults<R extends Representation> {
    private final R parameters;
    private final double gradientNorm;
    private final int iterationsPassed;

    public ParameterEstimatorResults(final R parameters, final double gradientNorm, final int iterationsPassed) {
        this.parameters = parameters;
        this.gradientNorm = gradientNorm;
        this.iterationsPassed = iterationsPassed;
    }

    public R getParameters() {
        return parameters;
    }

    public double getGradientNorm() {
        return gradientNorm;
    }

    public int getIterationsPassed() {
        return iterationsPassed;
    }
}
