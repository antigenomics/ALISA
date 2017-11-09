package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.LinearSpaceObjectUtils;

public class LowerTriangularMutableMatrix
        extends MutableRealMatrix {
    private final double[] elements;
    private final LowerTriangularDenseMatrix.LowerTriangularMatrixLinearIndexing matrixIndexing;

    public LowerTriangularMutableMatrix(double[] elements) {
        this.elements = elements;
        this.matrixIndexing = new LowerTriangularDenseMatrix.LowerTriangularMatrixLinearIndexing(elements.length);
    }

    @Override
    protected void plusInplaceUnchecked(RealMatrixAccessors other) {
        int k = 0;
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j <= i; j++) {
                elements[k] += other.getAt(i, j);
                k++;
            }
        }
    }

    @Override
    protected void minusInplaceUnchecked(RealMatrixAccessors other) {
        int k = 0;
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j <= i; j++) {
                elements[k] -= other.getAt(i, j);
                k++;
            }
        }
    }

    @Override
    public void multiplyInplace(double scalar) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] *= scalar;
        }
    }

    @Override
    public RealMatrix toImmutable() {
        return new LowerTriangularDenseMatrix(elements);
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
    public int getNumberOfRows() {
        return matrixIndexing.getNumberOfRows();
    }

    @Override
    public int getNumberOfColumns() {
        return matrixIndexing.getNumberOfRows();
    }

    @Override
    public double getAt(int i, int j) {
        return elements[matrixIndexing.getIndex(i, j)];
    }

    @Override
    public boolean isStrictlySymmetric() {
        return true;
    }
}
