package com.antigenomics.alisa.algebra;

import com.antigenomics.alisa.hamiltonian.Representation;

/**
 * A mutable extension of an immutable linear space objects that
 * adds methods to add and multiply a given object inplace.
 * @param <O> linear space object type
 */
public interface LinearSpaceObject<O extends LinearSpaceObject<O>>
        extends TypedCloneable<O>, ImmutableLinearSpaceObject<O>, Representation {
    /**
     *
     * @param other
     */
    void addInplace(final O other);

    /**
     *
     * @param scalar
     */
    void multiplyInplace(final double scalar);
}