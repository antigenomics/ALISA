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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterEstimatorResults<?> that = (ParameterEstimatorResults<?>) o;

        if (Double.compare(that.gradientNorm, gradientNorm) != 0) return false;
        if (iterationsPassed != that.iterationsPassed) return false;
        return parameters.equals(that.parameters);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = parameters.hashCode();
        temp = Double.doubleToLongBits(gradientNorm);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + iterationsPassed;
        return result;
    }
}
