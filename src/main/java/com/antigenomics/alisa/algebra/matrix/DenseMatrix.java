package com.antigenomics.alisa.algebra.matrix;


import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

import java.util.*;

/**
 * A two-dimensional matrix backed by a dense (primitive) array storage.
 * Fast operations with this matrix involve calling getAt() method. Working with this object as an
 * iterable of IndexedMatrixElement is slow as the inner array is copied to a list every time an iterator is called.
 * Dense matrix extends mutable linear object and can be scaled/added with matrices of the same row and column count.
 * It also extends a bilinear map object and can be used to map vectors to scalars via linear and
 * bilinear forms.
 */
public abstract class DenseMatrix extends Matrix {
    /* internal storage */
    protected final double[] elements;

    /* constructors */

    /**
     * A general purpose constructor of a dense matrix backed by a primitive array storage.
     *
     * @param elements        an array of matrix values
     * @param numberOfRows    number of rows
     * @param numberOfColumns number of columns
     * @param safe            if true will use a deep copy of the input array
     * @param lowerTriangular true if only lower triangular elements are stored
     */
    protected DenseMatrix(double[] elements,
                          int numberOfRows, int numberOfColumns,
                          boolean safe,
                          boolean lowerTriangular) {
        super(numberOfRows, numberOfColumns, false, lowerTriangular);
        if (safe) {
            this.elements = Arrays.copyOf(elements, elements.length);
        } else {
            this.elements = elements;
        }
    }

    /**
     * Create a new matrix of the same type (full/lower triangular)
     * from an array of elements
     *
     * @param elements an array of matrix values
     * @return a matrix
     */
    protected abstract Matrix withElements(double[] elements);

    /* overridden internal algebra operations */

    @Override
    public Matrix multiply(double scalar) {
        return withElements(LinearAlgebraUtils.scale(elements, scalar));
    }

    @Override
    public void multiplyInplace(double scalar) {
        LinearAlgebraUtils.scaleInplace(elements, scalar);
    }

    @Override
    protected double bilinearFormUncheckedDD(Vector a, Vector b) {
        double res = 0;
        if (a.getLength() > b.getLength()) {
            // outer loop should be ran for the smallest of vectors
            for (int j = 0; j < b.getLength(); j++) {
                final double bValue = b.getAt(j);
                if (bValue != 0) {
                    for (int i = 0; i < a.getLength(); i++) {
                        res += a.getAt(i) * getAt(i, j) * bValue;
                    }
                }
            }
        } else {
            for (int i = 0; i < a.getLength(); i++) {
                final double aValue = a.getAt(i);
                if (aValue != 0) {
                    for (int j = 0; j < b.getLength(); j++) {
                        res += aValue * getAt(i, j) * b.getAt(j);
                    }
                }
            }
        }
        return res;
    }

    @Override
    protected double bilinearFormUncheckedDS(Vector a, Vector b) {
        double res = 0;
        for (IndexedVectorValue bj : b) {
            final double bValue = bj.getDoubleValue();
            final int j = bj.getIndex();
            for (int i = 0; i < a.getLength(); i++) {
                res += a.getAt(i) * getAt(i, j) * bValue;
            }
        }
        return res;
    }

    @Override
    protected double bilinearFormUncheckedSD(Vector a, Vector b) {
        double res = 0;
        for (IndexedVectorValue ai : a) {
            final double value = ai.getDoubleValue();
            final int i = ai.getIndex();
            for (int j = 0; j < b.getLength(); j++) {
                res += value * getAt(i, j) * b.getAt(j);
            }
        }
        return res;
    }

    @Override
    protected double bilinearFormUncheckedSS(Vector a, Vector b) {
        double res = 0;
        if (a.getEffectiveSize() > b.getEffectiveSize()) {
            // outer loop should be ran for the smallest of vectors
            for (IndexedVectorValue bj : b) {
                final double bValue = bj.getDoubleValue();
                final int j = bj.getIndex();
                for (IndexedVectorValue ai : a) {
                    res += ai.getDoubleValue() * getAt(ai.getIndex(), j) * bValue;
                }
            }
        } else {
            for (IndexedVectorValue ai : a) {
                final double aValue = ai.getDoubleValue();
                final int i = ai.getIndex();
                for (IndexedVectorValue bj : b) {
                    res += aValue * getAt(i, bj.getIndex()) * bj.getDoubleValue();
                }
            }
        }
        return res;
    }

    @Override
    protected Vector linearFormUncheckedS(Vector b) {
        double[] resVector = new double[numberOfRows];
        for (IndexedVectorValue bj : b) {
            for (int i = 0; i < numberOfRows; i++) {
                resVector[i] += getAt(i, bj.getIndex()) * bj.getDoubleValue();
            }
        }
        return new DenseVector(resVector);
    }

    @Override
    protected Vector linearFormUncheckedD(Vector b) {
        double[] resVector = new double[numberOfRows];
        for (int j = 0; j < numberOfColumns; j++) {
            double bValue = b.getAt(j);
            if (bValue != 0) {
                for (int i = 0; i < numberOfRows; i++) {
                    resVector[i] += getAt(i, j) * bValue;
                }
            }
        }
        return new DenseVector(resVector);
    }

    @Override
    protected Matrix addUncheckedD(Matrix other) {
        double[] newElements = Arrays.copyOf(elements, elements.length);
        for (int i = 0; i < newElements.length; i++) {
            newElements[i] += other.getAt(i);
        }
        return withElements(newElements);
    }

    @Override
    protected Matrix addUncheckedS(Matrix other) {
        double[] newElements = Arrays.copyOf(elements, elements.length);
        for (IndexedMatrixValue e : other) {
            newElements[getLinearIndex(e.getRowIndex(), e.getColIndex())] += e.getDoubleValue();
        }
        return withElements(newElements);
    }

    @Override
    protected void addInplaceUncheckedD(Matrix other) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] += other.getAt(i);
        }
    }

    @Override
    protected void addInplaceUncheckedS(Matrix other) {
        for (IndexedMatrixValue e : other) {
            elements[getLinearIndex(e.getRowIndex(), e.getColIndex())] += e.getDoubleValue();
        }
    }

    /* overridden accessors */

    @Override
    public double getAt(int rowIndex, int columnIndex) {
        return elements[getLinearIndex(rowIndex, columnIndex)];
    }

    @Override
    protected double getAt(int linearIndex) {
        return elements[linearIndex];
    }

    protected abstract int getLinearIndex(int rowIndex, int columnIndex);

    @Override
    public Iterator<IndexedMatrixValue> iterator() {
        return indexValues(new ArrayList<>()).iterator();
    }

    /* overridden transformations */

    @Override
    public Matrix deepCopy() {
        return withElements(Arrays.copyOf(elements, elements.length));
    }

    @Override
    public Matrix asDense() {
        return deepCopy();
    }

    /* Internal auxiliary methods */

    /**
     * Adds all values of this vector to a pre-initialized list.
     *
     * @param storage an empty list
     * @return updated storage
     */
    protected List<IndexedMatrixValue> indexValues(List<IndexedMatrixValue> storage) {
        assert storage.isEmpty();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                double value = getAt(i, j);

                if (value != 0) {
                    storage.add(new IndexedMatrixValue(i, j, value));
                }
            }
        }

        return storage;
    }

    @Override
    public double norm1() {
        double norm1 = 0;

        for (double element : elements) {
            norm1 += Math.abs(element);
        }

        return norm1;
    }

    @Override
    public double norm2() {
        double norm2 = 0;

        for (double value : elements) {
            norm2 += value * value;
        }

        return Math.sqrt(norm2);
    }

    @Override
    public double normInf() {
        double normInf = 0;

        for (double element : elements) {
            normInf = Math.max(normInf, Math.abs(element));
        }

        return normInf;
    }
}
