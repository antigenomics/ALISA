package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.algebra.IndexingUtils;

public class TriangularMatrixIndexing
        implements MatrixIndexing {
    private final int size;

    public TriangularMatrixIndexing(int size) {
        this.size = size;
    }

    public static TriangularMatrixIndexing fromNumberOfElemets(int numberOfElements) {
        return new TriangularMatrixIndexing(IndexingUtils.getTriangularMatrixSize(numberOfElements));
    }

    @Override
    public int getIndex(int i, int j) {
        return IndexingUtils.getTriangularMatrixIndex(i, j);
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
