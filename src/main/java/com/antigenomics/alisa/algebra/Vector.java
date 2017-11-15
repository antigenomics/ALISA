package com.antigenomics.alisa.algebra;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;

/**
 * A generic one-dimensional real vector. The storage type (dense/sparse) depends on implementation.
 * Based on the storage type, fast access is implemented either via getAt (dense) or iterator (sparse) functions.
 * This class supports all linear algebra operations and can be used in linear and bilinear forms
 * implemented in Matrix objects. This class implements basic checks to guarantee that
 * the length of two vectors matches for operations where it is required.
 * Linear algebra methods tagged as in-place are not guaranteed to be thread safe.
 */
public abstract class Vector
        implements LinearSpaceObject<Vector>, VectorSpace<Vector, Matrix>,
        Container<IndexedVectorValue, Vector> {
    protected final int length;

    public static DenseVector fromValues(double... values) {
        return new DenseVector(values);
    }

    public static DenseVector zeros(int length) {
        return new DenseVector(new double[length]);
    }

    public static DenseVector ones(int length) {
        double[] arr = new double[length];
        Arrays.fill(arr, 1);
        return new DenseVector(arr);
    }

    public static DenseVector oneHotDense(int pos, int length) {
        double[] arr = new double[length];
        arr[pos] = 1;
        return new DenseVector(arr);
    }

    /**
     * Creates a vector with fixed length
     *
     * @param length vector length
     */
    protected Vector(int length) {
        this.length = length;
    }

    /**
     * Add elements of another vector to this vector and returns the resulting vector.
     * Contents of this vector remain unchanged.
     * This method should not check for vector length match.
     *
     * @param other vector to add
     * @return vector containing the elementwise sum
     */
    protected abstract Vector addUnchecked(@NotNull final Vector other);

    /**
     * Add elements of another vector in-place.
     * Contents of this vector are mutated.
     * This method should not check for vector length match.
     *
     * @param other vector to add
     */
    protected abstract void addInplaceUnchecked(@NotNull final Vector other);

    /**
     * A scalar product of two vectors,
     * that is the sum of products of elements having the same index.
     * This method should not check for vector length match.
     *
     * @param other vector
     * @return dot product value
     */
    protected abstract double dotProductUnchecked(@NotNull final Vector other);

    /**
     * Add elements of another vector to this vector and returns the resulting vector.
     * Contents of this vector remain unchanged.
     * Checks for vector length match.
     *
     * @param other vector of the same length to add
     * @return vector containing the elementwise sum
     */
    @Override
    public final Vector add(@NotNull final Vector other) {
        checkSizeMatch(other);
        return addUnchecked(other);
    }

    /**
     * Add elements of another vector in-place.
     * Contents of this vector are mutated.
     * Checks for vector length match.
     *
     * @param other vector of the same length to add
     */
    @Override
    public final void addInplace(@NotNull final Vector other) {
        if (this == other) {
            multiplyInplace(2.0);
        } else {
            checkSizeMatch(other);
            addInplaceUnchecked(other);
        }
    }

    /**
     * A scalar product of two vectors,
     * that is the sum of products of elements having the same index.
     * Checks for vector length match.
     *
     * @param other vector of the same length
     * @return dot product value
     */
    @Override
    public final double dotProduct(@NotNull final Vector other) {
        checkSizeMatch(other);
        return dotProductUnchecked(other);
    }

    /**
     * Check the length match between two vectors
     *
     * @param other vector to compare with
     */
    private void checkSizeMatch(@NotNull final Vector other) {
        if (length != other.length)
            throw new IllegalArgumentException("Vector lengths don't match");
    }

    /**
     * Gets the element at a given position of the vector
     *
     * @param index 0-based position index
     * @return element value
     */
    public abstract double getAt(final int index);

    /**
     * Gets the size of the vector
     *
     * @return vector length
     */
    public int getLength() {
        return length;
    }

    /**
     * @inheritdoc
     */
    @Override
    public double getAt(@NotNull final int... indices) {
        if (indices.length > 1)
            throw new IllegalArgumentException();
        return getAt(indices[0]);
    }

    /**
     * Computes the L1 norm of vector elements,
     * that is sum of their absolute values.
     *
     * @return L1-norm
     */
    @Override
    public double norm1() {
        double norm1 = 0;

        if (isSparse()) {
            for (IndexedVectorValue x : this) {
                norm1 += Math.abs(x.getDoubleValue());
            }
        } else {
            for (int i = 0; i < length; i++) {
                norm1 += Math.abs(getAt(i));
            }
        }

        return norm1;
    }

    /**
     * Computes the L2 norm of vector elements,
     * that is the square root of the sum of squares of their absolute values.
     *
     * @return L2-norm
     */
    @Override
    public double norm2() {
        double norm2 = 0;

        if (isSparse()) {
            for (IndexedVectorValue x : this) {
                norm2 += x.getDoubleValue() * x.getDoubleValue();
            }
        } else {
            for (int i = 0; i < length; i++) {
                double value = getAt(i);
                norm2 += value * value;
            }
        }

        return Math.sqrt(norm2);
    }
}
