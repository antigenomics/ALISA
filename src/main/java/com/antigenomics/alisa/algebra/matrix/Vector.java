package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.Container;
import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.algebra.VectorSpace;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    /* true vector size */
    protected final int length;

    /* auxiliary constructors */

    public static DenseVector DENSE(double... values) {
        return new DenseVector(values);
    }

    public static SparseVector SPARSE(double... values) {
        return new SparseVector(values);
    }

    public static DenseVector DENSE_ZEROS(int length) {
        return new DenseVector(new double[length]);
    }

    public static SparseVector SPARSE_ZEROS(int length) {
        return new SparseVector(new LinkedList<>(), length);
    }

    public static DenseVector DENSE_ONES(int length) {
        double[] arr = new double[length];
        Arrays.fill(arr, 1);
        return new DenseVector(arr);
    }

    public static DenseVector DENSE_ONEHOT(int pos, int length) {
        double[] arr = new double[length];
        arr[pos] = 1;
        return new DenseVector(arr);
    }

    public static SparseVector SPARSE_ONEHOT(int pos, int length) {
        List<IndexedVectorValue> elements = new LinkedList<>();
        elements.add(new IndexedVectorValue(pos, 1));
        return new SparseVector(elements, length);
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
    protected abstract Vector addUnchecked(final Vector other);

    /**
     * Add elements of another vector in-place.
     * Contents of this vector are mutated.
     * This method should not check for vector length match.
     *
     * @param other vector to add
     */
    protected abstract void addInplaceUnchecked(final Vector other);

    /**
     * A scalar product of two vectors,
     * that is the sum of products of elements having the same index.
     * This method should not check for vector length match.
     *
     * @param other vector
     * @return dot product value
     */
    protected abstract double dotProductUnchecked(final Vector other);

    /**
     * Add elements of another vector to this vector and returns the resulting vector.
     * Contents of this vector remain unchanged.
     * Checks for vector length match.
     *
     * @param other vector of the same length to add
     * @return vector containing the elementwise sum
     */
    @Override
    public final Vector add(final Vector other) {
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
    public final void addInplace(final Vector other) {
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
    public final double dotProduct(final Vector other) {
        checkSizeMatch(other);
        return dotProductUnchecked(other);
    }

    /**
     * Check the length match between two vectors
     *
     * @param other vector to compare with
     */
    private void checkSizeMatch(final Vector other) {
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
    public double getAt(final int... indices) {
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
