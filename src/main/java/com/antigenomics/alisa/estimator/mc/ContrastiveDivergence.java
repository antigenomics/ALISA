package com.antigenomics.alisa.estimator.mc;

import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.encoding.State;
import com.antigenomics.alisa.estimator.LogLikelihoodGradientAscent;
import com.antigenomics.alisa.hamiltonian.Hamiltonian;

import java.util.ArrayList;
import java.util.stream.Collector;

public final class ContrastiveDivergence<S extends State,
        R extends LinearSpaceObject<R>,
        H extends Hamiltonian<S, R>> extends LogLikelihoodGradientAscent<S, R, H> {
    private final MonteCarloSimulator<S, R, H> simulator;

    public ContrastiveDivergence(final double learningRate, final double tol, final int maxIter,
                                 final MonteCarloSimulator<S, R, H> simulator) {
        super(learningRate, tol, maxIter);
        this.simulator = simulator;
    }

    @Override
    protected R computeNetGradient(final ArrayList<S> states,
                                   final H hamiltonian,
                                   final R parameters) {
        return states.parallelStream()
                .map(s -> simulator.simulate(s, parameters, hamiltonian))
                .map(sL -> computeMcSimulationsGradient(sL, parameters, hamiltonian))
                .collect(Collector.of(
                        hamiltonian::getParameterGuess,
                        LinearSpaceObject::addInplace,
                        (result, newElement) -> {
                            newElement.addInplace(result);
                            return newElement;
                        }
                ));
    }

    private R computeMcSimulationsGradient(final ArrayList<S> states,
                                           final R parameters,
                                           final H hamiltonian) {
        final R xS = hamiltonian.getParameterGuess();

        for (S state : states) {
            xS.addInplace(hamiltonian.computeGradient(state, parameters));
        }

        xS.multiplyInplace(-1.0 / states.size());
        xS.addInplace(hamiltonian.computeGradient(states.get(0), parameters));

        return xS;
    }

    public MonteCarloSimulator<S, R, H> getSimulator() {
        return simulator;
    }
}