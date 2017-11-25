package com.antigenomics.alisa.algebra.matrix;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.StringJoiner;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.computeNumberOfFullMatrixRows;
import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.getFullMatrixIndex;

public final class DenseFullMatrix extends DenseMatrix {
    /**
     * Internal unsafe constructor
     *
     * @param elements        matrix values
     * @param numberOfColumns number of columns in matrix
     */
    protected DenseFullMatrix(double[] elements, int numberOfColumns) {
        this(elements, numberOfColumns, false);
    }

    /**
     * Creates a new dense matrix from a primitive array of elements.
     * The array is either copied or used as is depending on safe parameter. Setting safe to true also
     * results in checks for match between input array length and number of rows/columns.
     * Elements array stores matrix values in a linear way, i.e. first row is followed by the second and so on.
     *
     * @param elements        matrix values
     * @param numberOfColumns number of columns in resulting matrix
     * @param safe            if true will use a deep copy of the array
     */
    public DenseFullMatrix(double[] elements, int numberOfColumns, boolean safe) {
        super(elements, computeNumberOfFullMatrixRows(elements.length, numberOfColumns),
                numberOfColumns, safe, false);
        assert !safe || elements.length == numberOfRows * numberOfColumns;
    }

    protected DenseFullMatrix(double[][] elements) {
        super(new double[elements.length * elements[0].length],
                elements.length, elements[0].length, false, false);
        int k = 0;
        for (double[] row : elements) {
            for (int j = 0; j < numberOfColumns; j++) {
                this.elements[k++] = row[j];
            }
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
    public DenseFullMatrix(Iterable<IndexedMatrixValue> elements, int numberOfRows, int numberOfColumns) {
        this(new double[numberOfRows * numberOfColumns], numberOfColumns);
        for (IndexedMatrixValue e : elements) {
            int fullMatrixIndex = getFullMatrixIndex(e.getRowIndex(), e.getColIndex(), numberOfColumns);
            this.elements[fullMatrixIndex] = e.getDoubleValue();
        }
    }

    @Override
    protected Matrix withElements(double[] elements) {
        return new DenseFullMatrix(elements, numberOfColumns);
    }

    @Override
    protected int getLinearIndex(int rowIndex, int columnIndex) {
        return getFullMatrixIndex(rowIndex, columnIndex, numberOfColumns);
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

        return new DenseFullMatrix(newElements, numberOfRows);
    }

    @Override
    public Matrix asSparse() {
        return new SparseFullMatrix(indexValues(new LinkedList<>()),
                numberOfRows, numberOfColumns);
    }

    @Override
    public int getEffectiveSize() {
        int effectiveSize = 0;
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (elements[k++] != 0) {
                    effectiveSize++;
                }
            }
        }
        return effectiveSize;
    }

    /* overridden linear algebra operations */

    @Override
    protected double bilinearFormUncheckedS(Vector a) {
        return bilinearFormUncheckedSS(a, a);
    }

    @Override
    protected double bilinearFormUncheckedD(Vector a) {
        return bilinearFormUncheckedDD(a, a);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DenseFullMatrix that = (DenseFullMatrix) o;

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
