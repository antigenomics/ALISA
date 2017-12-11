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
     * Adds another object of the same type to this one in-place.
     * @param other linear space object
     */
    void addInplace(final O other);

    /**
     * Multiplies this linear object in-place by a given value.
     * @param scalar a value
     */
    void multiplyInplace(final double scalar);

    O asDense();
}