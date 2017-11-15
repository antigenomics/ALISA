package com.antigenomics.alisa.estimator;

import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.encoding.State;
import com.antigenomics.alisa.hamiltonian.Hamiltonian;

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

    protected abstract R computeNetGradient(final ArrayList<S> states,
                                            final H hamiltonian,
                                            final R parameters);

    @Override
    public ParameterEstimatorResults<R> learn(final ArrayList<S> states,
                                              final H hamiltonian,
                                              final R parametersGuess) {
        // todo: add logger

        int iter = 0;
        R parameters = parametersGuess.deepCopy();
        double norm = Double.MAX_VALUE;

        for (; iter < maxIter; iter++) {
            final R netGradient = computeNetGradient(states, hamiltonian, parameters);
            netGradient.multiplyInplace(learningRate / states.size());
            parameters.addInplace(netGradient);

            if ((norm = netGradient.norm2()) <= tol) {
                break;
            }
        }

        return new ParameterEstimatorResults<>(parameters, norm, iter);
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