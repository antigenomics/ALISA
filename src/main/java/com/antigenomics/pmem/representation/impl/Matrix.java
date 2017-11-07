package com.antigenomics.pmem.representation.impl;

public abstract class Matrix implements LinearSpaceObjectStorage<MatrixElement, Matrix> {
    protected final int size1, size2;

    public Matrix(final int size1, final int size2) {
        this.size1 = size1;
        this.size2 = size2;
    }

    protected abstract Matrix plusInner(Matrix other);

    public abstract double getAt(int i, int j);

    @Override
    public Matrix plus(Matrix other) {
        assertCompatible(other);
        return plusInner(other);
    }

    public abstract Vector getDiagonal();

    @Override
    public int[] getSizes() {
        return new int[]{size1, size2};
    }

    public abstract boolean isUpperTriangular();

    public boolean isSquare() {
        return size1 == size2;
    }

    public abstract double[] getRow(int i);

    public abstract double[] getColumn(int j);

    @Override
    public double getAt(int[] indices) {
        if (indices.length != 2) {
            throw new IllegalArgumentException("Two indices should be provided");
        }
        int i = indices[0], j = indices[1];
        if (i < 0 || i >= size1 || j < 0 || j >= size2) {
            throw new IndexOutOfBoundsException();
        }

        return getAt(i, j);
    }

    @Override
    public int getDimensions() {
        return 2;
    }

    public int getSize1() {
        return size1;
    }

    public int getSize2() {
        return size2;
    }

    public void assertVectorCompatibleRow(Vector other) {
        if (this.size1 != other.size) {
            throw new IllegalArgumentException("Vector size and number of rows don't match.");
        }
    }

    public void assertVectorCompatibleColumn(Vector other) {
        if (this.size2 != other.size) {
            throw new IllegalArgumentException("Vector size and number of columns don't match.");
        }
    }
}
