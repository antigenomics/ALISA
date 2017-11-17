package com.antigenomics.alisa.algebra;

import com.antigenomics.alisa.encoding.Encoding;

/**
 * A linear space object that supports addition and scaling, and has a norm.
 * Addition and scaling operations should map to a linear space object of the same type.
 *
 * @param <O> linear space object type
 */
public interface ImmutableLinearSpaceObject<O extends ImmutableLinearSpaceObject<O>>
        extends Encoding {
    /**
     * Add another linear space object of the same type to this one.
     * May throw an exception if object shapes don't match.
     * @param other object to add
     * @return addition result
     */
    O add(final O other);

    /**
     * Multiply this linear object by a scalar.
     * @param scalar scaling factor
     * @return scaling result
     */
    O multiply(final double scalar);

    /**
     * Computes 1-norm of the object. Commonly defined as $\sum_i |x_i|$.
     * @return 1-norm
     */
    double norm1();

    /**
     * Computes 2-norm of the object. Commonly defined as $\sum_i x_i^2$
     * @return 2-norm
     */
    double norm2();

    /**
     * Computes inf-norm of the object. Commonly defined as $\max_i |x_i|$
     * @return inf-norm
     */
    double normInf();
}