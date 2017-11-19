package com.antigenomics.alisa.algebra.matrix;

import java.util.*;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.*;

/**
 * A two-dimensional symmetric matrix backed by a dense (primitive) array storage. Only the lower triangle of
 * full square matrix is stored, i.e. only elements $x_{ij}$ with $i \leq j$.
 * Fast operations with this matrix involve calling getAt method and work for any i,j combination.
 * Working with this object as an iterable of IndexedMatrixElement is slow as the inner array is copied
 * to a list every time an iterator is called, moreover the iteration will run only across lower triangular
 * elements.
 * Dense symmetric matrix extends mutable linear object and can be scaled/added with matrices of the same row and column count.
 * It also extends a bilinear map object and can be used to map vectors to scalars via linear and
 * bilinear forms. Inplace addition only works if other matrix is also lower triangular.
 */
public class DenseTriangularMatrix extends DenseMatrix {
    /* constructors */

    /**
     * Internal unsafe constructor
     *
     * @param elements matrix values
     */
    protected DenseTriangularMatrix(double[] elements) {
        this(elements, false);
    }

    /**
     * Creates a new dense matrix from a primitive array of elements.
     * The array is either copied or used as is depending on safe parameter.
     * Elements array stores matrix values in a linear way, i.e. first element
     * followed by two second row elements and so on (all row elements up to diagonal element inclusive).
     * Matrix size is computed from the length of provided array,
     * an exception is thrown if resulting matrix is not square.
     *
     * @param elements matrix values
     * @param safe     if true will use a deep copy of the array
     */
    public DenseTriangularMatrix(double[] elements, final boolean safe) {
        super(elements,
                getTriangularMatrixSize(elements.length),
                getTriangularMatrixSize(elements.length),
                safe, true);
        if (safe) {
            assert numberOfRows == numberOfColumns;
            assert elements.length == getTriangularMatrixLength(numberOfRows);
        }
    }

    /**
     * Creates a new dense matrix from a list of indexed matrix values.
     * Elements array stores matrix values in a linear way, i.e. first row elements up to diagonal one
     * (inclusive) are followed by the second row elements and so on. All elements that lie above the diagonal
     * are discarded.
     * An exception is thrown if there are elements in the list that have row or column indices that are
     * out of bounds.
     *
     * @param elements     an interable of indexed matrix values
     * @param numberOfRows number of rows in resulting matrix
     */
    public DenseTriangularMatrix(Iterable<IndexedMatrixValue> elements, int numberOfRows) {
        this(new double[getTriangularMatrixLength(numberOfRows)]);
        for (IndexedMatrixValue e : elements) {
            if (e.getRowIndex() <= e.getColIndex()) {
                int index = getTriangularMatrixIndex(e.getRowIndex(), e.getColIndex());
                this.elements[index] = e.getDoubleValue();
            }
        }
    }

    @Override
    protected Matrix withElements(double[] elements) {
        return new DenseTriangularMatrix(elements);
    }

    @Override
    public double getAt(int rowIndex, int columnIndex) {
        return elements[getLinearIndex(rowIndex, columnIndex)];
    }

    @Override
    protected int getLinearIndex(int rowIndex, int columnIndex) {
        return getTriangularMatrixIndex(rowIndex, columnIndex);
    }

    @Override
    public Matrix transpose() {
        return this;
    }

    @Override
    public int getEffectiveSize() {
        int effectiveSize = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j <= i; j++) {
                if (getAt(i, j) != 0) {
                    effectiveSize++;
                }
            }
        }
        return effectiveSize;
    }

    /* overridden linear algebra operations */

    @Override
    protected double bilinearFormUncheckedS(Vector a) {
        double res = 0;

        for (IndexedVectorValue ai : a) {
            double aValue = ai.getDoubleValue();
            int i = ai.getIndex();

            for (IndexedVectorValue bj : a) {
                int j = bj.getIndex();
                if (j > i) {
                    break;
                }
                res += aValue * getAt(i, j) * bj.getDoubleValue();
            }
        }

        return res;
    }

    @Override
    protected double bilinearFormUncheckedD(Vector a) {
        double res = 0;

        for (int i = 0; i < a.getLength(); i++) {
            final double aValue = a.getAt(i);
            for (int j = 0; j <= i; j++) {
                res += aValue * getAt(i, j) * a.getAt(j);
            }
        }

        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DenseTriangularMatrix that = (DenseTriangularMatrix) o;

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
            for (int j = 0; j <= i; j++) {
                joiner.add(Float.toString((float) getAt(i, j)));
            }
            for (int j = i + 1; j < numberOfColumns; j++) {
                joiner.add("_");
            }
            res.append("[").append(joiner.toString()).append("]");
            if (i != numberOfRows - 1) {
                res.append("\n");
            }
        }

        return res.toString();
    }
}