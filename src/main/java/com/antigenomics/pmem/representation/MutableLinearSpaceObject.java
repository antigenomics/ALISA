package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public interface MutableLinearSpaceObject<R extends LinearSpaceObject<R>> extends LinearSpaceObject<R> {
    void plusInplaceUnchecked(@NotNull final R other);

    void minusInplaceUnchecked(@NotNull final R other);

    default void plusInplace(@NotNull final R other) {
        if (isEquivalent(other)) {
            plusInplace(other);
        } else {
            throw new IllegalArgumentException("Failed equivalence check between linear objects " + this + " and " + other);
        }
    }

    default void minusInplace(@NotNull final R other) {
        if (isEquivalent(other)) {
            minusInplace(other);
        } else {
            throw new IllegalArgumentException("Failed equivalence check between linear objects " + this + " and " + other);
        }
    }

    void multiplyInplace(final double scalar);

    R toImmutable();

    /**
     * Mock immutable. This method should not copy.
     *
     * @return
     */
    default R asImmutable() {
        return (R) this; // todo: just hide accessors; this should be enforced via middle abstract class for linearspaceobjectarray to work
    }

    default void plusInplace(@NotNull final MutableLinearSpaceObject<R> other) {
        plusInplace(other.asImmutable());
    }

    default void minusInplace(@NotNull final MutableLinearSpaceObject<R> other) {
        minusInplace(other.asImmutable());
    }

    @Override
    default double norm1() {
        return asImmutable().norm1();
    }

    @Override
    default double norm2() {
        return asImmutable().norm2();
    }

    @Override
    default boolean isEquivalent(R other) {
        return asImmutable().isEquivalent(other);
    }
}