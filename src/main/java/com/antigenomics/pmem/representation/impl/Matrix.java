package com.antigenomics.pmem.representation.impl;

import com.antigenomics.pmem.representation.LinearSpaceObject;

public abstract class Matrix implements LinearSpaceObjectStorage<MatrixElement, Matrix> {
    protected final int size1, size2;

    public Matrix(final int size1, final int size2) {
        this.size1 = size1;
        this.size2 = size2;
    }

    protected abstract Matrix plusInner(Matrix other);

    @Override
    public Matrix plus(Matrix other) {
        assertCompatible(other);
        return plusInner(other);
    }

    @Override
    public int[] getSizes() {
        return new int[]{size1, size2};
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
}
