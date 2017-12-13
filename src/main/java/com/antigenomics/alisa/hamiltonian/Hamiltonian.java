package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.encoding.State;

import java.util.stream.StreamSupport;

public interface Hamiltonian<S extends State, R extends Representation> {
    double computeEnergy(final S state, final R parameters);

    R computeGradient(final S state, final R parameters);

    default double computePartitionFunction(final Iterable<S> states, final R parameters) {
        return StreamSupport.stream(states.spliterator(), true)
                .mapToDouble(x -> x.getWeight() * Math.exp(computeEnergy(x, parameters))).sum();
    }

    R getZeroParameters();

    // this method should always copy
    R getParameterGuess();
}