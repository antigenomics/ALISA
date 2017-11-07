package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.encoding.Encoder;
import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.state.OneSiteState;
import com.sun.istack.internal.NotNull;

public final class OneSiteHamiltonianMatrix<T extends Entity, M extends Matrix<M>>
        implements Hamiltonian<OneSiteState<T>, M> {
    private final Encoder<T, M> encoder;
    private final M zeroParameters;

    public OneSiteHamiltonianMatrix(@NotNull final Encoder<T, M> encoder) {
        this.encoder = encoder;
        this.zeroParameters = encoder.getZero().transpose().multiply(encoder.getZero()).asSymmetric();
    }

    public Encoder<T, M> getEncoder() {
        return encoder;
    }

    @Override
    public double computeEnergy(@NotNull final OneSiteState<T> state, @NotNull final M parameters) {
        final M encoding = encoder.encode(state.getValue());
        return encoding.transpose().multiply(parameters).multiply(encoding).norm();
    }

    @Override
    public M computeGradient(@NotNull final OneSiteState<T> state, @NotNull final M parameters) {
        final M encoding = encoder.encode(state.getValue());
        return encoding.transpose().multiply(encoding).asSymmetric();
    }

    @Override
    public M getZeroParameters() {
        return zeroParameters;
    }
}