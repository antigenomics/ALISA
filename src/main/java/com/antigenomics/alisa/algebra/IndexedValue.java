package com.antigenomics.alisa.algebra;

/**
 * A generic indexed value that represents a single
 * element of vector, matrix or tensor. No explicit accessors
 * to indices are provided. Indexed values, however, can be
 * ordered using the Comparable interface method.
 *
 * @param <V> indexed value type
 */
public interface IndexedValue<V extends IndexedValue<V>>
        extends Comparable<V> {
    /**
     * Gets or converts the value of this object as double
     *
     * @return double value
     */
    double getDoubleValue();

    /**
     * Gets or converts the value of this object as integer
     *
     * @return integer value
     */
    int getIntValue();

    /**
     * Add other value to this one and return the result. Note that no
     * index comparison is performed, the returned value always inherit
     * indices of this object.
     *
     * @param other indexed value of the same type.
     * @return updated indexed value.
     */
    V add(V other);

    /**
     * Multiply the value by a scalar and return the result.
     *
     * @param scalar scale factor.
     * @return updated indexed value.
     */
    V scale(double scalar);
}
