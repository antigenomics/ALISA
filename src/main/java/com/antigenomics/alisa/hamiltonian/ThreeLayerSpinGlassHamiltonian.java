package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.*;
import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.state.ThreeLayerState;

import java.util.Arrays;

public final class ThreeLayerSpinGlassHamiltonian<E1 extends Entity, E2 extends Entity, E3 extends Entity,
        V extends VectorSpace<V, M> & ImmutableLinearSpaceObject<M>,
        M extends VectorMapping<V, M> & LinearSpaceObject<M>>
        implements SpinGlassHamiltonian<ThreeLayerState<E1, E2, E3>, V, M> {
    private final Encoder<E1, V> firstEncoder;
    private final Encoder<E2, V> secondEncoder;
    private final Encoder<E3, V> thirdEncoder;
    private final LinearSpaceObjectArray<M> zeroParameters;

    public ThreeLayerSpinGlassHamiltonian(final Encoder<E1, V> firstEncoder,
                                          final Encoder<E2, V> secondEncoder,
                                          final Encoder<E3, V> thirdEncoder) {
        this.firstEncoder = firstEncoder;
        this.secondEncoder = secondEncoder;
        this.thirdEncoder = thirdEncoder;
        final V z1 = firstEncoder.getZero(),
                z2 = secondEncoder.getZero(),
                z3 = thirdEncoder.getZero();
        this.zeroParameters = new LinearSpaceObjectArray<>(
                Arrays.asList(
                        z1.expand(),
                        z2.expand(),
                        z3.expand(),
                        z1.outerProduct(z2),
                        z2.outerProduct(z3),
                        z1.outerProduct(z3)
                )
        );
    }

    public Encoder<E1, V> getFirstEncoder() {
        return firstEncoder;
    }

    public Encoder<E2, V> getSecondEncoder() {
        return secondEncoder;
    }

    public Encoder<E3, V> getThirdEncoder() {
        return thirdEncoder;
    }

    @Override
    public double computeEnergy(final ThreeLayerState<E1, E2, E3> state,
                                final LinearSpaceObjectArray<M> parameters) {
        final V s1 = firstEncoder.encode(state.getFirstValue());
        final V s2 = secondEncoder.encode(state.getSecondValue());
        final V s3 = thirdEncoder.encode(state.getThirdValue());

        final M J1 = parameters.get(0),
                J2 = parameters.get(1),
                J3 = parameters.get(2),
                J12 = parameters.get(3),
                J23 = parameters.get(4),
                J13 = parameters.get(5);

        return J1.bilinearForm(s1, s1) +
                J2.bilinearForm(s2, s2) +
                J3.bilinearForm(s3, s3) +
                J12.bilinearForm(s1, s2) +
                J23.bilinearForm(s2, s3) +
                J13.bilinearForm(s1, s3);
    }

    @Override
    public LinearSpaceObjectArray<M> computeGradient(final ThreeLayerState<E1, E2, E3> state,
                                                     final LinearSpaceObjectArray<M> parameters) {
        final V s1 = firstEncoder.encode(state.getFirstValue());
        final V s2 = secondEncoder.encode(state.getSecondValue());
        final V s3 = thirdEncoder.encode(state.getThirdValue());

        return new LinearSpaceObjectArray<>(
                Arrays.asList(
                        s1.expand(),
                        s2.expand(),
                        s3.expand(),
                        s1.outerProduct(s2),
                        s2.outerProduct(s3),
                        s1.outerProduct(s3)
                )
        );
    }

    @Override
    public LinearSpaceObjectArray<M> getZeroParameters() {
        return zeroParameters;
    }
}