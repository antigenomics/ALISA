package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.encoding.VectorEncoding;

import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.encoding.OneLayerState;
import com.sun.istack.internal.NotNull;

import static java.util.Collections.singletonList;

public final class SingleLayerSpinGlassHamiltonian<E extends Entity,
        V extends VectorEncoding<V, M>,
        M extends MatrixRepresentation<V, M>>
        implements SpinGlassHamiltonian<OneLayerState<E>, V, M> {
    private final Encoder<E, V> encoder;
    private final LinearSpaceObjectArray<M> zeroParameters;

    public SingleLayerSpinGlassHamiltonian(@NotNull final Encoder<E, V> encoder) {
        this.encoder = encoder;
        this.zeroParameters = new LinearSpaceObjectArray<>(singletonList(encoder.getZero().expand()));
    }

    public Encoder<E, V> getEncoder() {
        return encoder;
    }

    @Override
    public double computeEnergy(@NotNull final OneLayerState<E> state,
                                @NotNull final LinearSpaceObjectArray<M> parameters) {
        final V encoding = encoder.encode(state.getValue());
        return parameters.get(0).bilinearForm(encoding, encoding);
    }

    @Override
    public LinearSpaceObjectArray<M> computeGradient(@NotNull final OneLayerState<E> state,
                                                     @NotNull final LinearSpaceObjectArray<M> parameters) {
        final V encoding = encoder.encode(state.getValue());
        return new LinearSpaceObjectArray<>(singletonList(encoding.outerProduct(encoding)));
    }

    @Override
    public LinearSpaceObjectArray<M> getNullParameters() {
        return zeroParameters;
    }
}