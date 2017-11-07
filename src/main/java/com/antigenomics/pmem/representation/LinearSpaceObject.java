package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public interface LinearSpaceObject<R extends LinearSpaceObject<R>> extends Representation {
    R plus(@NotNull final R other);

    R multiply(final double scalar);

    MutableLinearSpaceObject<R> toMutable();
}