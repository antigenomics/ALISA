package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.algebra.IndexingUtils;

public class FullMatrixIndexing
        implements MatrixIndexing {
    private final int numberOfColumns, numberOfRows;

    public FullMatrixIndexing(int numberOfElements, int numberOfColumns) {
        if (numberOfElements % numberOfColumns != 0) {
            throw new IllegalArgumentException("Number of elements should be a multiple of number of columns.");
        }

        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfElements / numberOfColumns;
    }

    @Override
    public int getIndex(int i, int j) {
        return IndexingUtils.getFullMatrixIndex(i, j, numberOfColumns);
    }

    @Override
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    @Override
    public boolean isStrictlySymmetric() {
        return false;
    }

    @Override
    public int getNumberOfRows() {
        return numberOfRows;
    }
}