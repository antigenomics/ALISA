package com.antigenomics.alisa.representation;

import com.sun.istack.internal.NotNull;

public interface MutableLinearSpaceObject<O extends ImmutableLinearSpaceObject<O>>
        extends HasNorm {
    void plusInplace(@NotNull final O other);

    void minusInplace(@NotNull final O other);

    void plusInplace(@NotNull final MutableLinearSpaceObject<O> other);

    void minusInplace(@NotNull final MutableLinearSpaceObject<O> other);

    void multiplyInplace(final double scalar);

    O toImmutable();
}