package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public class LinearSpaceObjectTrio<R extends LinearSpaceObject<R>, O extends LinearSpaceObjectTrio<R, O>>
        implements LinearSpaceObject<O> {
    private final R first, second, third;

    public LinearSpaceObjectTrio(@NotNull final R first, @NotNull final R second, @NotNull final R third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public O plus(@NotNull final O other) {
        return (O) new LinearSpaceObjectTrio(
                first.plus(other.getFirst()),
                second.plus(other.getSecond()),
                third.plus(other.getThird())
        );
    }

    @Override
    public O multiply(double scalar) {
        return (O) new LinearSpaceObjectTrio(
                first.multiply(scalar),
                second.multiply(scalar),
                third.multiply(scalar)
        );
    }

    @Override
    public MutableLinearSpaceObject<O> toMutable() {
        return new MutableLinearSpaceObjectTrio(
                first.toMutable(),
                second.toMutable(),
                third.toMutable());
    }

    @Override
    public double norm() {
        double n1 = first.norm(),
                n2 = second.norm(),
                n3 = third.norm();
        return Math.sqrt(n1 * n1 + n2 * n2 + n3 * n3); // todo: defaulting to L2 here
    }

    public R getFirst() {
        return first;
    }

    public R getSecond() {
        return second;
    }

    public R getThird() {
        return third;
    }

    private class MutableLinearSpaceObjectTrio
            extends LinearSpaceObjectTrio<R, O>
            implements MutableLinearSpaceObject<O> {
        private final MutableLinearSpaceObject<R> firstMutable,
                secondMutable,
                thirdMutable;

        MutableLinearSpaceObjectTrio(@NotNull final MutableLinearSpaceObject<R> firstMutable,
                                     @NotNull final MutableLinearSpaceObject<R> secondMutable,
                                     @NotNull final MutableLinearSpaceObject<R> thirdMutable) {
            super(firstMutable.asImmutable(),
                    secondMutable.asImmutable(),
                    thirdMutable.asImmutable());
            this.firstMutable = firstMutable;
            this.secondMutable = secondMutable;
            this.thirdMutable = thirdMutable;
        }

        @Override
        public void plusInplace(@NotNull final O other) {
            firstMutable.plusInplace(other.getFirst());
            secondMutable.plusInplace(other.getSecond());
            thirdMutable.plusInplace(other.getThird());
        }

        @Override
        public void minusInplace(@NotNull final O other) {
            firstMutable.minusInplace(other.getFirst());
            secondMutable.minusInplace(other.getSecond());
            thirdMutable.minusInplace(other.getThird());
        }

        @Override
        public void multiplyInplace(double scalar) {
            firstMutable.multiplyInplace(scalar);
            secondMutable.multiplyInplace(scalar);
            thirdMutable.multiplyInplace(scalar);
        }

        @Override
        public O toImmutable() {
            return (O) new LinearSpaceObjectTrio(
                    firstMutable.toImmutable(),
                    secondMutable.toImmutable(),
                    thirdMutable.toImmutable());
        }
    }
}
