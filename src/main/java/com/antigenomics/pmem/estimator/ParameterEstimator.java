package com.antigenomics.pmem.estimator;


import com.antigenomics.pmem.state.State;
import com.antigenomics.pmem.hamiltonian.Hamiltonian;
import com.antigenomics.pmem.representation.ImmutableLinearSpaceObject;

import java.util.ArrayList;

public interface ParameterEstimator<S extends State,
        R extends ImmutableLinearSpaceObject, H extends Hamiltonian<S, R>> {
    default ParameterEstimatorResults learn(final ArrayList<S> states, final H hamiltonian) {
        return learn(states, hamiltonian, hamiltonian.getZeroParameters());
    }

    ParameterEstimatorResults<R> learn(final ArrayList<S> states, final H hamiltonian, final R parameterGuess);
}