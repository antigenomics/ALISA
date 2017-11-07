package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public class LinearSpaceObjectPair<R extends LinearSpaceObject<R>, O extends LinearSpaceObjectPair<R, O>>
        implements LinearSpaceObject<O> {
    private final R first, second;

    public LinearSpaceObjectPair(@NotNull final R first, @NotNull final R second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public O plus(@NotNull final O other) {
        return (O) new LinearSpaceObjectPair(
                first.plus(other.getFirst()),
                second.plus(other.getSecond())
        );
    }

    @Override
    public O multiply(double scalar) {
        return (O) new LinearSpaceObjectPair(
                first.multiply(scalar),
                second.multiply(scalar)
        );
    }

    @Override
    public MutableLinearSpaceObject<O> toMutable() {
        return new MutableLinearSpaceObjectPair(
                first.toMutable(),
                second.toMutable()
        );
    }

    @Override
    public double norm() {
        double n1 = first.norm(), n2 = second.norm();
        return Math.sqrt(n1 * n1 + n2 * n2); // todo: defaulting to L2 here
    }

    public R getFirst() {
        return first;
    }

    public R getSecond() {
        return second;
    }

    private class MutableLinearSpaceObjectPair
            extends LinearSpaceObjectPair<R, O>
            implements MutableLinearSpaceObject<O> {
        private final MutableLinearSpaceObject<R> firstMutable, secondMutable;

        MutableLinearSpaceObjectPair(@NotNull final MutableLinearSpaceObject<R> firstMutable,
                                     @NotNull final MutableLinearSpaceObject<R> secondMutable) {
            super(firstMutable.asImmutable(), secondMutable.asImmutable());
            this.firstMutable = firstMutable;
            this.secondMutable = secondMutable;
        }

        @Override
        public void plusInplace(@NotNull final O other) {
            firstMutable.plusInplace(other.getFirst());
            secondMutable.plusInplace(other.getSecond());
        }

        @Override
        public void minusInplace(O other) {
            firstMutable.minusInplace(other.getFirst());
            secondMutable.minusInplace(other.getSecond());
        }

        @Override
        public void multiplyInplace(double scalar) {
            firstMutable.multiplyInplace(scalar);
            secondMutable.multiplyInplace(scalar);
        }

        @Override
        public O toImmutable() {
            return (O) new LinearSpaceObjectPair(
                    firstMutable.toImmutable(),
                    secondMutable.toImmutable()
            );
        }
    }
}