package com.antigenomics.pmem.estimator;


import com.antigenomics.pmem.state.State;
import com.antigenomics.pmem.hamiltonian.Hamiltonian;
import com.antigenomics.pmem.representation.LinearSpaceObject;

import java.util.ArrayList;

public interface ParameterEstimator<S extends State,
        R extends LinearSpaceObject, H extends Hamiltonian<S, R>> {
    default ParameterEstimatorResults learn(final ArrayList<S> states, final H hamiltonian) {
        return learn(states, hamiltonian, hamiltonian.getZeroParameters());
    }

    ParameterEstimatorResults<R> learn(final ArrayList<S> states, final H hamiltonian, final R parameterGuess);
}