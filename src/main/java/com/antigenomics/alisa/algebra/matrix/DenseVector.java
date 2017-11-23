package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

import java.util.*;

/**
 * A one-dimensional vector backed by a dense (primitive) array storage.
 * Fast operations with this vector involve calling getAt method. Working with this object as an
 * iterable of IndexedVectorElement is slow as the inner array is copied to a list every time an iterator is called.
 * Dense vector extends mutable linear object and can be scaled/added with vectors of the same length.
 * It also extends a vector space object and can be used in scalar and outer product with other vectors,
 * as well as linear, bilinear and symmetric bilinear forms with matrices.
 */
public final class DenseVector extends Vector {
    /* internal storage */
    private final double[] elements;

    /**
     * Internal unsafe constructor
     *
     * @param elements vector elements
     */
    protected DenseVector(final double[] elements) {
        this(elements, false);
    }

    /**
     * Creates an instance of DenseVector from a primitive array.
     * Vector length is equal to the array length.
     * The array is either copied or used as is depending on safe parameter.
     *
     * @param elements vector values
     * @param safe     if true will use a deep copy of the array
     */
    public DenseVector(final double[] elements, final boolean safe) {
        super(elements.length);
        if (safe) {
            this.elements = Arrays.copyOf(elements, length);
        } else {
            this.elements = elements;
        }
    }

    /**
     * Creates an instance of DenseVector from a list of indexed elements.
     * The elements are copied to a primitive array, length of the array is specified
     * separately.
     *
     * @param elementList a list of indexed vector values
     * @param length      vector length
     * @throws IndexOutOfBoundsException if the index of any of elements is greater or equal to the length
     */
    public DenseVector(final List<IndexedVectorValue> elementList, final int length) {
        super(length);
        this.elements = new double[length];

        for (IndexedVectorValue indexedVectorValue : elementList) {
            elements[indexedVectorValue.getIndex()] = indexedVectorValue.getDoubleValue();
        }
    }


    @Override
    protected Vector addUnchecked(Vector other) {
        double[] newElements = Arrays.copyOf(elements, elements.length);
        addImpl(newElements, other);
        return new DenseVector(newElements);
    }


    @Override
    protected void addInplaceUnchecked(Vector other) {
        addImpl(elements, other);
    }


    @Override
    protected double dotProductUnchecked(Vector other) {
        double res = 0;

        if (other.isSparse()) {
            for (IndexedVectorValue e : other) {
                res += elements[e.getIndex()] * e.getDoubleValue();
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                res += elements[i] * other.getAt(i);
            }
        }

        return res;
    }


    @Override
    public Vector multiply(double scalar) {
        return new DenseVector(LinearAlgebraUtils.scale(elements, scalar));
    }


    @Override
    public void multiplyInplace(double scalar) {
        LinearAlgebraUtils.scaleInplace(elements, scalar);
    }


    @Override
    public Matrix outerProduct(Vector b) {
        int numberOfColumns = b.getLength();
        double[] matElements = new double[elements.length * numberOfColumns];

        int k = 0;
        for (double element : elements) {
            for (int j = 0; j < numberOfColumns; j++) {
                matElements[k] = element * b.getAt(j);
                k++;
            }
        }

        return new DenseFullMatrix(matElements, numberOfColumns);
    }


    @Override
    public Matrix expand() {
        double[] matElements = new double[LinearAlgebraUtils.getTriangularMatrixLength(elements.length)];

        int k = 0;
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j <= i; j++) {
                matElements[k] = elements[i] * elements[j];
                k++;
            }
        }

        return new DenseTriangularMatrix(matElements);
    }


    @Override
    public double getAt(int index) {
        return elements[index];
    }


    @Override
    public Iterator<IndexedVectorValue> iterator() {
        return indexValues(new ArrayList<>()).iterator();
    }


    @Override
    public boolean isSparse() {
        return false;
    }


    @Override
    public int getEffectiveSize() {
        int effectiveSize = 0;

        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != 0)
                effectiveSize++;
        }

        return effectiveSize;
    }


    @Override
    public Vector deepCopy() {
        return new DenseVector(Arrays.copyOf(elements, elements.length));
    }


    @Override
    public Vector asSparse() {
        return new SparseVector(indexValues(new LinkedList<>()), length);
    }


    @Override
    public Vector asDense() {
        return deepCopy();
    }

    /* Internal auxiliary methods */

    /**
     * Adds all values of this vector to a pre-initialized list.
     *
     * @param storage an empty list of indexed values
     * @return updated storage
     */
    private List<IndexedVectorValue> indexValues(final List<IndexedVectorValue> storage) {
        assert storage.isEmpty();

        for (int i = 0; i < elements.length; i++) {
            double value = elements[i];

            if (value != 0) {
                storage.add(new IndexedVectorValue(i, value));
            }
        }

        return storage;
    }

    /**
     * Adds (elementwise) values of a vector to a pre-initialized array
     *
     * @param elements primitive array, pre-initialized
     * @param other    other vector, either sparse or dense
     */
    private static void addImpl(final double[] elements, final Vector other) {
        if (other.isSparse()) {
            for (IndexedVectorValue e : other) {
                elements[e.getIndex()] += e.getDoubleValue();
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                elements[i] += other.getAt(i);
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DenseVector that = (DenseVector) o;

        return Arrays.equals(elements, that.elements);
    }


    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for (double element : elements) {
            joiner.add(Float.toString((float)element));
        }
        return "[" + joiner.toString() + "]";
    }
}