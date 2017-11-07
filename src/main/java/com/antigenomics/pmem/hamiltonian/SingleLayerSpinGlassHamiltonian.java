package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.encoding.Encoder;
import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.representation.algebra.BilinearComposition;
import com.antigenomics.pmem.representation.algebra.VectorSpace;
import com.antigenomics.pmem.state.OneSiteState;
import com.sun.istack.internal.NotNull;

public final class OneLayerSpingGlassHamiltonian<T extends Entity,
        V extends VectorSpace<V, M>,
        M extends BilinearComposition<V, M>>
        implements Hamiltonian<OneSiteState<T>, M> {
    private final Encoder<T, V> encoder;
    private final M zeroParameters;

    public OneLayerSpingGlassHamiltonian(@NotNull final Encoder<T, V> encoder) {
        this.encoder = encoder;
        this.zeroParameters = encoder.getZero().outerProduct(encoder.getZero());
    }

    public Encoder<T, V> getEncoder() {
        return encoder;
    }

    @Override
    public double computeEnergy(@NotNull final OneSiteState<T> state,
                                @NotNull final M parameters) {
        final V encoding = encoder.encode(state.getValue());
        return parameters.bilinearForm(encoding, encoding);
    }

    @Override
    public M computeGradient(@NotNull final OneSiteState<T> state,
                             @NotNull final M parameters) {
        final V encoding = encoder.encode(state.getValue());
        return encoding.outerProduct(encoding);
    }

    @Override
    public M getZeroParameters() {
        return zeroParameters;
    }
}