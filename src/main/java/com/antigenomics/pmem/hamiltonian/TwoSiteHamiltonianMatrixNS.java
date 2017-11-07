package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.encoding.Encoder;
import com.antigenomics.pmem.representation.LinearSpaceObjectTrio;
import com.antigenomics.pmem.representation.Matrix;
import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.state.TwoSiteStateNS;
import com.sun.istack.internal.NotNull;

public class TwoSiteHamiltonianMatrixNS<T extends Entity, U extends Entity, M extends Matrix<M>>
        implements Hamiltonian<TwoSiteStateNS<T, U>, LinearSpaceObjectTrio<M>> {
    private final Encoder<T, M> firstEncoder;
    private final Encoder<U, M> secondEncoder;
    private final LinearSpaceObjectTrio<M> zeroParameters;

    public TwoSiteHamiltonianMatrixNS(@NotNull final Encoder<T, M> firstEncoder,
                                      @NotNull final Encoder<U, M> secondEncoder) {
        this.firstEncoder = firstEncoder;
        this.secondEncoder = secondEncoder;
        if (firstEncoder == secondEncoder) {
            final M x = firstEncoder.getZero().transpose().multiply(firstEncoder.getZero()).asSymmetric();
            this.zeroParameters = new LinearSpaceObjectTrio<>(x, x, x);
        } else {
            final M x1 = firstEncoder.getZero().transpose().multiply(firstEncoder.getZero()).asSymmetric(),
                    x2 = secondEncoder.getZero().transpose().multiply(secondEncoder.getZero()).asSymmetric(),
                    x3 = firstEncoder.getZero().transpose().multiply(secondEncoder.getZero()).asSymmetric();
            this.zeroParameters = new LinearSpaceObjectTrio<>(x1, x2, x3);
        }
    }

    @Override
    public double computeEnergy(@NotNull final TwoSiteStateNS<T, U> state,
                                @NotNull final LinearSpaceObjectTrio<M> parameters) {
        final M x1 = firstEncoder.encode(state.getFirstValue()),
                x2 = secondEncoder.encode(state.getSecondValue());

        return x1.transpose().multiply(parameters.getFirst()).multiply(x1).norm() +
                x2.transpose().multiply(parameters.getSecond()).multiply(x2).norm() +
                x1.transpose().multiply(parameters.getThird()).multiply(x2).norm();
    }

    @Override
    public LinearSpaceObjectTrio<M> computeGradient(@NotNull final TwoSiteStateNS<T, U> state,
                                                         @NotNull final LinearSpaceObjectTrio<M> parameters) {
        final M x1 = firstEncoder.encode(state.getFirstValue()),
                x2 = secondEncoder.encode(state.getSecondValue());

        return new LinearSpaceObjectTrio<>(
                x1.transpose().multiply(x1).asSymmetric(),
                x2.transpose().multiply(x2).asSymmetric(),
                x1.transpose().multiply(x2).asSymmetric());
    }

    @Override
    public LinearSpaceObjectTrio<M> getZeroParameters() {
        return zeroParameters;
    }
}