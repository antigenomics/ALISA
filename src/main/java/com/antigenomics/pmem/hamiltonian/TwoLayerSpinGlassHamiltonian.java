package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.encoding.Encoder;
import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.representation.LinearSpaceObjectArray;
import com.antigenomics.pmem.representation.algebra.BilinearComposition;
import com.antigenomics.pmem.representation.algebra.VectorSpace;
import com.antigenomics.pmem.state.OneLayerState;
import com.sun.istack.internal.NotNull;

import static java.util.Collections.singletonList;

public final class SingleLayerSpinGlassHamiltonian<T extends Entity,
        V extends VectorSpace<V, M>,
        M extends BilinearComposition<V, M>>
        implements SpinGlassHamiltonian<OneLayerState<T>, V, M> {
    private final Encoder<T, V> encoder;
    private final LinearSpaceObjectArray<M> zeroParameters;

    public SingleLayerSpinGlassHamiltonian(@NotNull final Encoder<T, V> encoder) {
        this.encoder = encoder;
        this.zeroParameters = new LinearSpaceObjectArray<>(singletonList(encoder.getZero().expand()));
    }

    public Encoder<T, V> getEncoder() {
        return encoder;
    }

    @Override
    public double computeEnergy(@NotNull final OneLayerState<T> state,
                                @NotNull final LinearSpaceObjectArray<M> parameters) {
        final V encoding = encoder.encode(state.getValue());
        return parameters.get(0).bilinearForm(encoding, encoding);
    }

    @Override
    public LinearSpaceObjectArray<M> computeGradient(@NotNull final OneLayerState<T> state,
                                                     @NotNull final LinearSpaceObjectArray<M> parameters) {
        final V encoding = encoder.encode(state.getValue());
        return new LinearSpaceObjectArray<>(singletonList(encoding.outerProduct(encoding)));
    }

    @Override
    public LinearSpaceObjectArray<M> getZeroParameters() {
        return zeroParameters;
    }
}