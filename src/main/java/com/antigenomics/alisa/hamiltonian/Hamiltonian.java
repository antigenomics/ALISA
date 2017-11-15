package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.encoding.State;
import com.sun.istack.internal.NotNull;

public interface Hamiltonian<S extends State, R extends Representation> {
    double computeEnergy(@NotNull final S state, @NotNull final R parameters);

    R computeGradient(@NotNull final S state, @NotNull final R parameters);

    R getNullParameters();
}