package com.antigenomics.alisa.algebra;

import com.antigenomics.alisa.hamiltonian.Representation;
import com.sun.istack.internal.NotNull;

public interface LinearSpaceObject<O extends LinearSpaceObject<O>>
        extends TypedCloneable<O>, ImmutableLinearSpaceObject<O>, Representation {
    void addInplace(@NotNull final O other);

    void multiplyInplace(final double scalar);
}