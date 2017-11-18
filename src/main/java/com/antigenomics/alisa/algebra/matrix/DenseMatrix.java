package com.antigenomics.alisa.algebra.matrix;


import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

import java.util.*;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.*;

/**
 * A two-dimensional matrix backed by a dense (primitive) array storage.
 * Fast operations with this matrix involve calling getAt() method. Working with this object as an
 * iterable of IndexedMatrixElement is slow as the inner array is copied to a list every time an iterator is called.
 * Dense matrix extends mutable linear object and can be scaled/added with matrices of the same row and column count.
 * It also extends a bilinear map object and can be used to map vectors to scalars via linear and
 * bilinear forms.
 */
public class DenseMatrix
        extends Matrix {
    /* internal storage */
    private final double[] elements;

    /**
     * Internal unsafe constructor
     *
     * @param elements        matrix values
     * @param numberOfColumns number of columns in matrix
     */
    protected DenseMatrix(final double[] elements, final int numberOfColumns) {
        this(elements, numberOfColumns, false);
    }

    /**
     * Creates a new dense matrix from a primitive array of elements.
     * The array is either copied or used as is depending on safe parameter.
     * Elements array stores matrix values in a linear way, i.e. first row is followed by the second and so on.
     * The number of rows is computed from the length of provided array and number of columns,
     * an exception is thrown if lengths are discordant.
     *
     * @param elements        matrix values
     * @param numberOfColumns number of columns in resulting matrix
     * @param safe            if true will use a deep copy of the array
     */
    public DenseMatrix(final double[] elements, final int numberOfColumns, final boolean safe) {
        super(computeNumberOfFullMatrixRows(elements.length, numberOfColumns), numberOfColumns);
        if (safe) {
            this.elements = Arrays.copyOf(elements, elements.length);
        } else {
            this.elements = elements;
        }
    }

    /**
     * Creates a new dense matrix from a list of indexed matrix values.
     * Elements array stores matrix values in a linear way, i.e. first row is followed by the second and so on.
     * An exception is thrown if there are elements in the list that have row or column indices that are
     * out of bounds.
     *
     * @param elements        an interable of indexed matrix values
     * @param numberOfRows    number of rows in resulting matrix
     * @param numberOfColumns number of columns in resulting matrix
     */
    public DenseMatrix(Iterable<IndexedMatrixValue> elements, int numberOfRows, int numberOfColumns) {
        super(numberOfRows, numberOfColumns);
        this.elements = new double[numberOfRows * numberOfColumns];
        for (IndexedMatrixValue e : elements) {
            int fullMatrixIndex = getFullMatrixIndex(e.getRowIndex(), e.getColIndex(), numberOfColumns);
            this.elements[fullMatrixIndex] = e.getDoubleValue();
        }
    }


    @Override
    public double getAt(int rowIndex, int columnIndex) {
        return elements[getFullMatrixIndex(rowIndex, columnIndex, numberOfColumns)];
    }


    @Override
    protected double getAt(int linearIndex) {
        return elements[linearIndex];
    }


    @Override
    public Matrix transpose() {
        double[] newElements = new double[elements.length];

        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                newElements[getFullMatrixIndex(j, i, numberOfRows)] = elements[k];
                k++;
            }
        }

        return new DenseMatrix(newElements, numberOfRows);
    }


    @Override
    protected final double bilinearFormUnchecked(Vector a) {
        if (a.isSparse()) {
            return bfSS2(a, a);
        } else {
            return bfDD2(a, a);
        }
    }


    @Override
    protected Vector linearFormUnchecked(Vector b) {
        double[] resVector = new double[numberOfRows];
        if (b.isSparse()) {
            for (IndexedVectorValue bj : b) {
                for (int i = 0; i < numberOfRows; i++) {
                    resVector[i] += getAt(i, bj.getIndex()) * bj.getDoubleValue();
                }
            }
        } else {
            for (int j = 0; j < numberOfColumns; j++) {
                double bValue = b.getAt(j);
                if (bValue != 0) {
                    for (int i = 0; i < numberOfRows; i++) {
                        resVector[i] += getAt(i, j) * bValue;
                    }
                }
            }
        }
        return new DenseVector(resVector);
    }


    @Override
    protected double bilinearFormUnchecked(Vector a, Vector b) {
        if (a.isSparse()) {
            if (b.isSparse()) {
                return bfSS(a, b);
            } else {
                return bfSD(a, b);
            }
        } else if (b.isSparse()) {
            return bfDS(a, b);
        } else {
            return bfDD(a, b);
        }
    }


    @Override
    protected Matrix addUnchecked(Matrix other) {
        double[] newElements = Arrays.copyOf(elements, elements.length);
        addImpl(newElements, other);
        return new DenseMatrix(newElements, numberOfColumns);
    }


    @Override
    protected void addInplaceUnchecked(Matrix other) {
        addImpl(elements, other);
    }


    @Override
    public Matrix multiply(double scalar) {
        return new DenseMatrix(LinearAlgebraUtils.scale(elements, scalar),
                numberOfColumns);
    }

    @Override
    public void multiplyInplace(double scalar) {
        LinearAlgebraUtils.scaleInplace(elements, scalar);
    }

    @Override
    public Matrix deepCopy() {
        return new DenseMatrix(Arrays.copyOf(elements, elements.length),
                numberOfColumns);
    }


    @Override
    public Iterator<IndexedMatrixValue> iterator() {
        return indexValues(new ArrayList<>()).iterator();
    }


    @Override
    public boolean isSparse() {
        return false;
    }


    @Override
    public int getEffectiveSize() {
        int effectiveSize = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (getAt(i, j) != 0) {
                    effectiveSize++;
                }
            }
        }
        return effectiveSize;
    }


    @Override
    public Matrix asSparse() {
        return new SparseMatrix(indexValues(new LinkedList<>()),
                numberOfRows, numberOfColumns);
    }


    @Override
    public Matrix asDense() {
        return deepCopy();
    }

    /* Internal auxiliary methods */

    /**
     * Adds all values of this vector to a pre-initialized list.
     *
     * @param storage an empty list of indexed values
     * @return updated storage
     */
    private List<IndexedMatrixValue> indexValues(final List<IndexedMatrixValue> storage) {
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

    /**
     * Internal. Bilinear form for two sparse vectors.
     * Calls bfSS2 if first vector has a larger effective size than the second one
     *
     * @param a first vector
     * @param b second vector
     * @return scalar value
     */
    private double bfSS(Vector a, Vector b) {
        if (a.getEffectiveSize() > b.getEffectiveSize()) {
            // outer loop should be ran for the smallest of vectors
            return bfSS2(a, b);
        }

        double res = 0;
        for (IndexedVectorValue ai : a) {
            final double aValue = ai.getDoubleValue();
            final int i = ai.getIndex();
            for (IndexedVectorValue bj : b) {
                res += aValue * getAt(i, bj.getIndex()) * bj.getDoubleValue();
            }
        }
        return res;
    }

    /**
     * Internal. Bilinear form for two sparse vectors.
     * Second vector should have smaller effective size than the first one
     *
     * @param a first vector
     * @param b second vector
     * @return scalar value
     */
    private double bfSS2(Vector a, Vector b) {
        double res = 0;
        for (IndexedVectorValue bj : b) {
            final double bValue = bj.getDoubleValue();
            final int j = bj.getIndex();
            for (IndexedVectorValue ai : a) {
                res += ai.getDoubleValue() * getAt(ai.getIndex(), j) * bValue;
            }
        }
        return res;
    }

    /**
     * Internal. Bilinear form for dense and sparse vector pair.
     *
     * @param a sparse vector
     * @param b dense vector
     * @return scalar value
     */
    private double bfSD(Vector a, Vector b) {
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

    /**
     * Internal. Bilinear form for dense and sparse vector pair.
     *
     * @param a dense vector
     * @param b sparse vector
     * @return scalar value
     */
    private double bfDS(Vector a, Vector b) {
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

    /**
     * Internal. Bilinear form for two dense vectors.
     * Calls bfDD2 if first vector is longer than the second one
     *
     * @param a first vector
     * @param b second vector
     * @return scalar value
     */
    private double bfDD(Vector a, Vector b) {
        if (a.getLength() > b.getLength()) {
            // outer loop should be ran for the smallest of vectors
            return bfSS2(a, b);
        }

        double res = 0;
        for (int i = 0; i < a.getLength(); i++) {
            final double aValue = a.getAt(i);
            if (aValue != 0) {
                for (int j = 0; j < b.getLength(); j++) {
                    res += aValue * getAt(i, j) * b.getAt(j);
                }
            }
        }
        return res;
    }

    /**
     * Internal. Bilinear form for two dense vectors.
     * Second vector should be shorter than the first one
     *
     * @param a first vector
     * @param b second vector
     * @return scalar value
     */
    private double bfDD2(Vector a, Vector b) {
        double res = 0;
        for (int j = 0; j < b.getLength(); j++) {
            final double bValue = b.getAt(j);
            if (bValue != 0) {
                for (int i = 0; i < a.getLength(); i++) {
                    res += a.getAt(i) * getAt(i, j) * bValue;
                }
            }
        }
        return res;
    }

    /**
     * Internal. Adds values from a given matrix to a pre-initialized storage array.
     * Supports adding dense, sparse, full and triangular matrices.
     *
     * @param elements pre-initialized array, to be modified in-place
     * @param other    matrix to add
     */
    private void addImpl(double[] elements, Matrix other) {
        if (other.isSparse()) {
            if (other.isLowerTriangular) {
                for (IndexedMatrixValue e : other) {
                    int i = e.getRowIndex(), j = e.getColIndex();
                    elements[getFullMatrixIndex(i, j, numberOfColumns)] += e.getDoubleValue();
                    if (i != j) {
                        elements[getFullMatrixIndex(j, i, numberOfColumns)] += e.getDoubleValue();
                    }
                }
            } else {
                for (IndexedMatrixValue e : other) {
                    elements[getFullMatrixIndex(e.getRowIndex(), e.getColIndex(), numberOfColumns)] += e.getDoubleValue();
                }
            }
        } else {
            if (other.isLowerTriangular) {
                int k = 0;
                for (int i = 0; i < numberOfRows; i++) {
                    for (int j = 0; j < numberOfColumns; j++) {
                        elements[k] += other.getAt(i, j);
                        k++;
                    }
                }
            } else {
                for (int i = 0; i < elements.length; i++) {
                    elements[i] += other.getAt(i);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DenseMatrix that = (DenseMatrix) o;

        return Arrays.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < numberOfRows; i++) {
            StringJoiner joiner = new StringJoiner(", ");
            for (int j = 0; j < numberOfColumns; j++) {
                joiner.add(Float.toString((float) getAt(i, j)));
            }
            res.append("[").append(joiner.toString()).append("]");
            if (i != numberOfRows - 1) {
                res.append("\n");
            }
        }

        return res.toString();
    }
}
