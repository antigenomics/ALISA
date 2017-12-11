package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.*;
import com.antigenomics.alisa.encoding.VectorEncoding;

import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.encoding.OneLayerState;

import static java.util.Collections.singletonList;

public final class SingleLayerSpinGlassHamiltonian<E extends Entity,
        V extends VectorSpace<V, M> & ImmutableLinearSpaceObject<M>,
        M extends VectorMapping<V, M> & LinearSpaceObject<M>>
        implements SpinGlassHamiltonian<OneLayerState<E>, V, M> {
    private final Encoder<E, V> encoder;
    private final LinearSpaceObjectArray<M> nullParameters;

    public SingleLayerSpinGlassHamiltonian(final Encoder<E, V> encoder) {
        this.encoder = encoder;
        this.nullParameters = new LinearSpaceObjectArray<>(singletonList(encoder.getZero().expand()));
    }

    public Encoder<E, V> getEncoder() {
        return encoder;
    }

    @Override
    public double computeEnergy(final OneLayerState<E> state,
                                final LinearSpaceObjectArray<M> parameters) {
        final V encoding = encoder.encode(state.getValue());
        return parameters.get(0).bilinearForm(encoding, encoding);
    }

    @Override
    public LinearSpaceObjectArray<M> computeGradient(final OneLayerState<E> state,
                                                     final LinearSpaceObjectArray<M> parameters) {
        final V encoding = encoder.encode(state.getValue());
        return new LinearSpaceObjectArray<>(singletonList(encoding.outerProduct(encoding)));
    }

    @Override
    public LinearSpaceObjectArray<M> getZeroParameters() {
        return nullParameters;
    }
}