package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public interface LinearSpaceObject<R extends LinearSpaceObject<R>> extends Representation {
    R plusUnchecked(@NotNull final R other);

    default R plus(@NotNull final R other) {
        if (isEquivalent(other)) {
            return plusUnchecked(other);
        }
        throw new IllegalArgumentException("Failed equivalence check between linear objects " + this + " and " + other);
    }

    R multiply(final double scalar);

    MutableLinearSpaceObject<R> toMutable();

    double norm2();

    double norm1();

    boolean isEquivalent(R other);
}