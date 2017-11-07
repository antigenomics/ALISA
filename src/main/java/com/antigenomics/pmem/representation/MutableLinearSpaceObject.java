package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public interface MutableLinearSpaceObject<R extends LinearSpaceObject<R>> extends LinearSpaceObject<R> {
    void plusInplace(@NotNull final R other);

    void minusInplace(@NotNull final R other);

    void multiplyInplace(final double scalar);

    R toImmutable();

    /**
     * Mock immutable.
     *
     * @return
     */
    default R asImmutable() {
        return (R) this; // todo: just hide accessors
    }

    default void plusInplace(@NotNull final MutableLinearSpaceObject<R> other) {
        plusInplace(other.asImmutable());
    }

    default void minusInplace(@NotNull final MutableLinearSpaceObject<R> other) {
        minusInplace(other.asImmutable());
    }

    @Override
    default double norm() {
        return asImmutable().norm();
    }
}