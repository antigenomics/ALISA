package com.antigenomics.alisa.algebra;

import com.antigenomics.alisa.hamiltonian.Representation;

public interface LinearSpaceObject<O extends LinearSpaceObject<O>>
        extends TypedCloneable<O>, ImmutableLinearSpaceObject<O>, Representation {
    void addInplace(final O other);

    void multiplyInplace(final double scalar);
}