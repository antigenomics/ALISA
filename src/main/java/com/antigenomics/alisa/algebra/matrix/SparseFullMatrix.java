package com.antigenomics.alisa.algebra.matrix;

import java.util.LinkedList;
import java.util.List;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.computeNumberOfFullMatrixRows;

public final class SparseFullMatrix extends SparseMatrix {
    protected SparseFullMatrix(List<IndexedMatrixValue> elementList,
                               int numberOfRows, int numberOfColumns) {
        this(elementList, numberOfRows, numberOfColumns, false);
    }

    protected SparseFullMatrix(double[] elements, int numberOfColumns) {
        super(new LinkedList<>(), computeNumberOfFullMatrixRows(elements.length, numberOfColumns),
                numberOfColumns, false, false);
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                double value = elements[k++];
                if (value != 0) {
                    elementList.add(new IndexedMatrixValue(i, j, value));
                }
            }
        }
    }

    protected SparseFullMatrix(double[][] elements) {
        super(new LinkedList<>(), elements.length, elements[0].length, false, false);
        for (int i = 0; i < numberOfRows; i++) {
            double[] row = elements[i];
            for (int j = 0; j < numberOfColumns; j++) {
                double value = row[j];
                if (value != 0) {
                    elementList.add(new IndexedMatrixValue(i, j, value));
                }
            }
        }
    }

    /**
     * Creates a new dense matrix from a list of indexed matrix values.
     * Elements array stores matrix values in a linear way, i.e. first row is followed by the second and so on.
     * An exception is thrown if there are elements in the list that have row or column indices that are
     * out of bounds.
     *
     * @param elementList     a list of indexed matrix values
     * @param numberOfRows    number of rows in resulting matrix
     * @param numberOfColumns number of columns in resulting matrix
     */
    public SparseFullMatrix(List<IndexedMatrixValue> elementList,
                            int numberOfRows, int numberOfColumns,
                            boolean safe) {
        super(elementList, numberOfRows, numberOfColumns, safe, false);
    }

    @Override
    protected Matrix withElements(List<IndexedMatrixValue> elementList) {
        return new SparseFullMatrix(elementList, numberOfRows, numberOfColumns);
    }
}
