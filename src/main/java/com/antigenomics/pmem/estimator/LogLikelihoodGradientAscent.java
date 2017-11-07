package com.antigenomics.pmem.estimator;

import com.antigenomics.pmem.state.State;
import com.antigenomics.pmem.hamiltonian.Hamiltonian;
import com.antigenomics.pmem.representation.LinearSpaceObject;
import com.antigenomics.pmem.representation.MutableLinearSpaceObject;

import java.util.ArrayList;

public abstract class LogLikelihoodGradientAscent<S extends State,
        R extends LinearSpaceObject<R>,
        H extends Hamiltonian<S, R>>
        implements ParameterEstimator<S, R, H> {
    private final double learningRate, tol;
    private final int maxIter;

    public LogLikelihoodGradientAscent(final double learningRate, final double tol, final int maxIter) {
        this.learningRate = learningRate;
        this.tol = tol;
        this.maxIter = maxIter;
    }

    protected abstract MutableLinearSpaceObject<R> computeNetGradient(final ArrayList<S> states,
                                                                      final H hamiltonian,
                                                                      final MutableLinearSpaceObject<R> parameters);

    @Override
    public ParameterEstimatorResults<R> learn(final ArrayList<S> states,
                                              final H hamiltonian,
                                              final R parametersGuess) {
        final MutableLinearSpaceObject<R> parameters = parametersGuess.toMutable();

        int iter = 0;
        double norm = Double.MAX_VALUE;

        for (; iter < maxIter; iter++) {
            final MutableLinearSpaceObject<R> netGradient = computeNetGradient(states, hamiltonian, parameters);
            netGradient.multiplyInplace(-learningRate / states.size());
            parameters.plusInplace(netGradient);

            if ((norm = parameters.norm()) <= tol) {
                break;
            }
        }

        return new ParameterEstimatorResults<>(parameters.toImmutable(), norm, iter);
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getTol() {
        return tol;
    }

    public int getMaxIter() {
        return maxIter;
    }
}