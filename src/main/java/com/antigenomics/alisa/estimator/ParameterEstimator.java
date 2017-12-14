package com.antigenomics.alisa.estimator;

import com.antigenomics.alisa.hamiltonian.Representation;
import com.antigenomics.alisa.state.State;
import com.antigenomics.alisa.hamiltonian.Hamiltonian;

import java.util.ArrayList;

public interface ParameterEstimator<S extends State, R extends Representation, H extends Hamiltonian<S, R>> {
    default ParameterEstimatorResults learn(final ArrayList<S> states, final H hamiltonian) {
        return learn(states, hamiltonian, hamiltonian.createParameterGuess());
    }

    ParameterEstimatorResults<R> learn(final ArrayList<S> states, final H hamiltonian, final R parameterGuess);
}