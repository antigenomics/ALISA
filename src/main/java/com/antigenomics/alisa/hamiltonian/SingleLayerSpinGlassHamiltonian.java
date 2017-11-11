package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.representation.ImmutableLSOArray;
import com.antigenomics.alisa.representation.algebra.BilinearMap;
import com.antigenomics.alisa.representation.algebra.VectorSpace;
import com.antigenomics.alisa.state.OneLayerState;
import com.sun.istack.internal.NotNull;

import static java.util.Collections.singletonList;

public final class SingleLayerSpinGlassHamiltonian<E extends Entity,
        V extends VectorSpace<V, M>,
        M extends BilinearMap<V, M>>
        implements SpinGlassHamiltonian<OneLayerState<E>, V, M> {
    private final Encoder<E, V> encoder;
    private final ImmutableLSOArray<M> zeroParameters;

    public SingleLayerSpinGlassHamiltonian(@NotNull final Encoder<E, V> encoder) {
        this.encoder = encoder;
        this.zeroParameters = new ImmutableLSOArray<>(singletonList(encoder.getZero().expand()));
    }

    public Encoder<E, V> getEncoder() {
        return encoder;
    }

    @Override
    public double computeEnergy(@NotNull final OneLayerState<E> state,
                                @NotNull final ImmutableLSOArray<M> parameters) {
        final V encoding = encoder.encode(state.getValue());
        return parameters.get(0).bilinearForm(encoding, encoding);
    }

    @Override
    public ImmutableLSOArray<M> computeGradient(@NotNull final OneLayerState<E> state,
                                                @NotNull final ImmutableLSOArray<M> parameters) {
        final V encoding = encoder.encode(state.getValue());
        return new ImmutableLSOArray<>(singletonList(encoding.outerProduct(encoding)));
    }

    @Override
    public ImmutableLSOArray<M> getZeroParameters() {
        return zeroParameters;
    }
}