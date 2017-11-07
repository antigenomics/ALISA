package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.encoding.Encoder;
import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.representation.algebra.BilinearComposition;
import com.antigenomics.pmem.representation.algebra.VectorSpace;
import com.antigenomics.pmem.state.OneSiteState;
import com.sun.istack.internal.NotNull;

public final class OneSiteHamiltonianMatrix<T extends Entity>
        implements Hamiltonian<OneSiteState<T>, BilinearComposition> {
    private final Encoder<T, VectorSpace> encoder;
    private final BilinearComposition zeroParameters;

    public OneSiteHamiltonianMatrix(@NotNull final Encoder<T, VectorSpace> encoder) {
        this.encoder = encoder;
        this.zeroParameters = encoder.getZero().outerProduct(encoder.getZero());
    }

    public Encoder<T, VectorSpace> getEncoder() {
        return encoder;
    }

    @Override
    public double computeEnergy(@NotNull final OneSiteState<T> state,
                                @NotNull final BilinearComposition parameters) {
        final VectorSpace encoding = encoder.encode(state.getValue());
        return parameters.bilinearForm(encoding, encoding);
    }

    @Override
    public BilinearComposition computeGradient(@NotNull final OneSiteState<T> state,
                                               @NotNull final BilinearComposition parameters) {
        final VectorSpace encoding = encoder.encode(state.getValue());
        return encoding.outerProduct(encoding);
    }

    @Override
    public BilinearComposition getZeroParameters() {
        return zeroParameters;
    }
}