package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.*;

import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.state.OneLayerState;

import static java.util.Collections.singletonList;

public final class SingleLayerSpinGlassHamiltonian<E extends Entity,
        V extends VectorSpace<V, M> & ImmutableLinearSpaceObject<M>,
        M extends VectorMapping<V, M> & LinearSpaceObject<M>>
        implements SpinGlassHamiltonian<OneLayerState<E>, V, M> {
    private final Encoder<E, V> encoder;
    private final LinearSpaceObjectArray<M> zeroParameters;

    public SingleLayerSpinGlassHamiltonian(final Encoder<E, V> encoder) {
        this.encoder = encoder;
        this.zeroParameters = new LinearSpaceObjectArray<>(singletonList(encoder.getZero().expand()));
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
        return zeroParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SingleLayerSpinGlassHamiltonian<?, ?, ?> that = (SingleLayerSpinGlassHamiltonian<?, ?, ?>) o;

        if (!encoder.equals(that.encoder)) return false;
        return zeroParameters.equals(that.zeroParameters);
    }

    @Override
    public int hashCode() {
        int result = encoder.hashCode();
        result = 31 * result + zeroParameters.hashCode();
        return result;
    }
}