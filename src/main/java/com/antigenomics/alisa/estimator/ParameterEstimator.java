package com.antigenomics.alisa.estimator;


import com.antigenomics.alisa.state.State;
import com.antigenomics.alisa.hamiltonian.Hamiltonian;
import com.antigenomics.alisa.representation.ImmutableLinearSpaceObject;

import java.util.ArrayList;

public interface ParameterEstimator<S extends State,
        R extends ImmutableLinearSpaceObject, H extends Hamiltonian<S, R>> {
    default ParameterEstimatorResults learn(final ArrayList<S> states, final H hamiltonian) {
        return learn(states, hamiltonian, hamiltonian.getZeroParameters());
    }

    ParameterEstimatorResults<R> learn(final ArrayList<S> states, final H hamiltonian, final R parameterGuess);
}