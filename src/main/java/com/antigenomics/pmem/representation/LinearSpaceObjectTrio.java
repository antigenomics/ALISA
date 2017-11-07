package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public class LinearSpaceObjectTrio<R extends LinearSpaceObject<R>>
        implements LinearSpaceObject<LinearSpaceObjectTrio<R>> {
    private final R first, second, third;

    public LinearSpaceObjectTrio(@NotNull final R first, @NotNull final R second, @NotNull final R third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public LinearSpaceObject<LinearSpaceObjectTrio<R>> plus(@NotNull final LinearSpaceObject<LinearSpaceObjectTrio<R>> other) {
        return new LinearSpaceObjectTrio<>(
                (R) first.plus(((LinearSpaceObjectTrio<R>) other).first),
                (R) second.plus(((LinearSpaceObjectTrio<R>) other).second),
                (R) third.plus(((LinearSpaceObjectTrio<R>) other).third));
    }

    @Override
    public LinearSpaceObject<LinearSpaceObjectTrio<R>> multiply(double scalar) {
        return new LinearSpaceObjectTrio<>(
                (R) first.multiply(scalar),
                (R) second.multiply(scalar),
                (R) third.multiply(scalar));
    }

    @Override
    public MutableLinearSpaceObject<LinearSpaceObjectTrio<R>> toMutable() {
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
            extends LinearSpaceObjectTrio<R>
            implements MutableLinearSpaceObject<LinearSpaceObjectTrio<R>> {
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
        public void plusInplace(@NotNull final LinearSpaceObjectTrio<R> other) {
            firstMutable.plusInplace(other.first);
            secondMutable.plusInplace(other.second);
            thirdMutable.plusInplace(other.third);
        }

        @Override
        public void minusInplace(@NotNull final LinearSpaceObjectTrio<R> other) {
            firstMutable.minusInplace(other.first);
            secondMutable.minusInplace(other.second);
            thirdMutable.minusInplace(other.third);
        }

        @Override
        public void multiplyInplace(double scalar) {
            firstMutable.multiplyInplace(scalar);
            secondMutable.multiplyInplace(scalar);
            thirdMutable.multiplyInplace(scalar);
        }

        @Override
        public LinearSpaceObjectTrio<R> toImmutable() {
            return new LinearSpaceObjectTrio<>(
                    firstMutable.toImmutable(),
                    secondMutable.toImmutable(),
                    thirdMutable.toImmutable());
        }
    }
}
