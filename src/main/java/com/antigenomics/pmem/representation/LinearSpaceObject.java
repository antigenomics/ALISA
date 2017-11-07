package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

public interface LinearSpaceObject<R extends LinearSpaceObject<R>> extends Representation {
    LinearSpaceObject<R> plus(@NotNull final LinearSpaceObject<R> other);

    LinearSpaceObject<R> multiply(final double scalar);

    MutableLinearSpaceObject<R> toMutable();
}
