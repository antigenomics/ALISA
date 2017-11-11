package com.antigenomics.alisa.estimator;

import com.antigenomics.alisa.state.State;
import com.antigenomics.alisa.hamiltonian.Hamiltonian;
import com.antigenomics.alisa.representation.ImmutableLinearSpaceObject;
import com.antigenomics.alisa.representation.MutableLinearSpaceObject;

import java.util.ArrayList;

public abstract class LogLikelihoodGradientAscent<S extends State,
        R extends ImmutableLinearSpaceObject<R>,
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
                                                                      final R parameters);

    @Override
    public ParameterEstimatorResults<R> learn(final ArrayList<S> states,
                                              final H hamiltonian,
                                              final R parametersGuess) {
        // todo: add logger

        int iter = 0;
        R parameters = parametersGuess;
        double norm = Double.MAX_VALUE;
        final MutableLinearSpaceObject<R> mutableParameters = parametersGuess.toMutable();

        for (; iter < maxIter; iter++) {
            final MutableLinearSpaceObject<R> netGradient = computeNetGradient(states, hamiltonian, parameters);
            netGradient.multiplyInplace(-learningRate / states.size());

            mutableParameters.plusInplace(netGradient);

            parameters = mutableParameters.toImmutable();

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