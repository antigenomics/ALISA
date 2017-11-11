package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.algebra.IndexingUtils;

public class LowerTriangularMatrixIndexing
        implements MatrixIndexing {
    private final int size;

    public LowerTriangularMatrixIndexing(int numberOfElements) {
        this.size = IndexingUtils.getTriangularMatrixSize(numberOfElements);
    }

    @Override
    public int getIndex(int i, int j) {
        if (j > i) {
            return getIndex(j, i);
        } else {
            return IndexingUtils.getTriangularMatrixLength(i) + j;
        }
    }

    @Override
    public int getNumberOfRows() {
        return size;
    }

    @Override
    public int getNumberOfColumns() {
        return size;
    }

    @Override
    public boolean isStrictlySymmetric() {
        return true;
    }
}
