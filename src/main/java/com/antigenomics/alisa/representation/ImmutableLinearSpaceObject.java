package com.antigenomics.alisa.representation;

import com.sun.istack.internal.NotNull;

public interface ImmutableLinearSpaceObject<O extends ImmutableLinearSpaceObject<O>>
        extends Representation, HasNorm {
    O plus(@NotNull final O other);

    O multiply(final double scalar);

    MutableLinearSpaceObject<O> toMutable();
}