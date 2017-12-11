package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.encoding.State;

public interface Hamiltonian<S extends State, R extends Representation> {
    double computeEnergy(final S state, final R parameters);

    R computeGradient(final S state, final R parameters);

    R getZeroParameters();

    // this method should always copy
    R getParameterGuess();
}