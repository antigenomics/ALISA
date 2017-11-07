package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public class LinearSpaceObjectPair<R extends LinearSpaceObject<R>>
        implements LinearSpaceObject<LinearSpaceObjectPair<R>> {
    private final R first, second;

    public LinearSpaceObjectPair(@NotNull final R first, @NotNull final R second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public LinearSpaceObject<LinearSpaceObjectPair<R>> plus(@NotNull final LinearSpaceObject<LinearSpaceObjectPair<R>> other) {
        return new LinearSpaceObjectPair<>(
                (R) first.plus(((LinearSpaceObjectPair<R>) other).first),
                (R) second.plus(((LinearSpaceObjectPair<R>) other).second));
    }

    @Override
    public LinearSpaceObject<LinearSpaceObjectPair<R>> multiply(double scalar) {
        return new LinearSpaceObjectPair<>(
                (R) first.multiply(scalar),
                (R) second.multiply(scalar));
    }

    @Override
    public MutableLinearSpaceObject<LinearSpaceObjectPair<R>> toMutable() {
        return new MutableLinearSpaceObjectPair(first.toMutable(), second.toMutable());
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
            extends LinearSpaceObjectPair<R>
            implements MutableLinearSpaceObject<LinearSpaceObjectPair<R>> {
        private final MutableLinearSpaceObject<R> firstMutable, secondMutable;

        MutableLinearSpaceObjectPair(@NotNull final MutableLinearSpaceObject<R> firstMutable,
                                     @NotNull final MutableLinearSpaceObject<R> secondMutable) {
            super(firstMutable.asImmutable(),
                    secondMutable.asImmutable());
            this.firstMutable = firstMutable;
            this.secondMutable = secondMutable;
        }

        @Override
        public void plusInplace(@NotNull final LinearSpaceObjectPair<R> other) {
            firstMutable.plusInplace(other.first);
            secondMutable.plusInplace(other.second);
        }

        @Override
        public void minusInplace(LinearSpaceObjectPair<R> other) {
            firstMutable.minusInplace(other.first);
            secondMutable.minusInplace(other.second);
        }

        @Override
        public void multiplyInplace(double scalar) {
            firstMutable.multiplyInplace(scalar);
            secondMutable.multiplyInplace(scalar);
        }

        @Override
        public LinearSpaceObjectPair<R> toImmutable() {
            return new LinearSpaceObjectPair<>(firstMutable.toImmutable(),
                    secondMutable.toImmutable());
        }
    }
}