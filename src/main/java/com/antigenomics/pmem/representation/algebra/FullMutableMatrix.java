package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.LinearSpaceObjectUtils;

import java.util.Arrays;

public class FullMutableMatrix
        extends MutableRealMatrix {
    private final double[] elements;
    private final int numberOfColumns, numberOfRows;

    public FullMutableMatrix(double[] elements, int numberOfColumns) {
        this.elements = elements;
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = elements.length / numberOfColumns;
    }

    @Override
    protected void plusInplaceUnchecked(RealMatrixAccessors other) {
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                elements[k] += other.getAt(i, j);
                k++;
            }
        }
    }

    @Override
    protected void minusInplaceUnchecked(RealMatrixAccessors other) {
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
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
        return new FullDenseMatrix(Arrays.copyOf(elements, elements.length), numberOfColumns);
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
    public int getSize1() {
        return numberOfRows;
    }

    @Override
    public int getSize2() {
        return numberOfColumns;
    }

    @Override
    public double getAt(int i, int j) {
        return i * numberOfColumns + j;
    }

    @Override
    public boolean isStrictlySymmetric() {
        return false;
    }
}
