package com.antigenomics.alisa.estimator;

import com.antigenomics.alisa.hamiltonian.Representation;

public final class ParameterEstimatorResults<R extends Representation> {
    private final R parameters;
    private final double gradientNorm;
    private final int iterationsPassed;

    public ParameterEstimatorResults(final R parameters,
                                     final double absoluteToleranceReached,
                                     final int iterationsPassed) {
        this.parameters = parameters;
        this.gradientNorm = absoluteToleranceReached;
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
