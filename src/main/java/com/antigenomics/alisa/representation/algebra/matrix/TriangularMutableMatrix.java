package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.LinearSpaceObjectUtils;

public final class TriangularMutableMatrix
        extends MutableRealMatrix {
    private final double[] elements;
    private final TriangularMatrixIndexing indexing;

    public TriangularMutableMatrix(double[] elements) {
        this.elements = elements;
        this.indexing = TriangularMatrixIndexing.fromNumberOfElemets(elements.length);
    }

    @Override
    protected void plusInplaceUnchecked(RealMatrixAccessor other) {
        int k = 0;
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j <= i; j++) {
                elements[k] += other.getAt(i, j);
                k++;
            }
        }
    }

    @Override
    protected void minusInplaceUnchecked(RealMatrixAccessor other) {
        int k = 0;
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j <= i; j++) {
                elements[k] -= other.getAt(i, j);
                k++;
            }
        }
    }

    @Override
    protected MatrixIndexing getIndexing() {
        return indexing;
    }

    @Override
    public void multiplyInplace(double scalar) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] *= scalar;
        }
    }

    @Override
    public RealMatrix toImmutable() {
        return new TriangularDenseMatrix(elements);
    }

    @Override
    public double norm1() {
        return LinearSpaceObjectUtils.norm1(elements);
    }

    @Override
    public double norm2() {
        return LinearSpaceObjectUtils.norm2(elements);
    }

    @Override
    public double getAt(int i, int j) {
        return elements[indexing.getIndex(i, j)];
    }
}
