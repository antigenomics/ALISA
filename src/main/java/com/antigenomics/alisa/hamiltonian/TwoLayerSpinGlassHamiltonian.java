package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.encoding.VectorEncoding;
import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.encoding.TwoLayerState;
import com.sun.istack.internal.NotNull;

import java.util.Arrays;

public final class TwoLayerSpinGlassHamiltonian<E1 extends Entity, E2 extends Entity,
        V extends VectorEncoding<V, M>,
        M extends MatrixRepresentation<V, M>>
        implements SpinGlassHamiltonian<TwoLayerState<E1, E2>, V, M> {
    private final Encoder<E1, V> firstEncoder;
    private final Encoder<E2, V> secondEncoder;
    private final LinearSpaceObjectArray<M> zeroParameters;

    public TwoLayerSpinGlassHamiltonian(@NotNull final Encoder<E1, V> firstEncoder,
                                        @NotNull final Encoder<E2, V> secondEncoder) {
        this.firstEncoder = firstEncoder;
        this.secondEncoder = secondEncoder;
        final V z1 = firstEncoder.getZero(),
                z2 = secondEncoder.getZero();
        this.zeroParameters = new LinearSpaceObjectArray<>(
                Arrays.asList(
                        z1.expand(),
                        z2.expand(),
                        z1.outerProduct(z2)
                )
        );
    }

    public Encoder<E1, V> getFirstEncoder() {
        return firstEncoder;
    }

    public Encoder<E2, V> getSecondEncoder() {
        return secondEncoder;
    }

    @Override
    public double computeEnergy(@NotNull final TwoLayerState<E1, E2> state,
                                @NotNull final LinearSpaceObjectArray<M> parameters) {
        final V s1 = firstEncoder.encode(state.getFirstValue());
        final V s2 = secondEncoder.encode(state.getSecondValue());

        final M J1 = parameters.get(0),
                J2 = parameters.get(1),
                J12 = parameters.get(2);

        return J1.bilinearForm(s1, s1) +
                J2.bilinearForm(s2, s2) +
                J12.bilinearForm(s1, s2);
    }

    @Override
    public LinearSpaceObjectArray<M> computeGradient(@NotNull final TwoLayerState<E1, E2> state,
                                                     @NotNull final LinearSpaceObjectArray<M> parameters) {
        final V s1 = firstEncoder.encode(state.getFirstValue());
        final V s2 = secondEncoder.encode(state.getSecondValue());

        return new LinearSpaceObjectArray<>(
                Arrays.asList(
                        s1.expand(),
                        s2.expand(),
                        s1.outerProduct(s2)
                )
        );
    }

    @Override
    public LinearSpaceObjectArray<M> getNullParameters() {
        return zeroParameters;
    }
}