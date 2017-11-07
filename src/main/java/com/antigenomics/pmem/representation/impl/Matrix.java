package com.antigenomics.pmem.representation.impl;

import com.antigenomics.pmem.representation.LinearSpaceObject;

public abstract class Matrix implements LinearSpaceObjectStorage<MatrixElement, Matrix> {
    protected final int[] sizes;

    public Matrix(final int[] sizes) {
        this.sizes = sizes;
    }

    protected abstract Matrix plusInner(Matrix other);

    @Override
    public LinearSpaceObject<Matrix> plus(LinearSpaceObject<Matrix> other) {
        assertCompatible((Matrix) other);
        return plusInner((Matrix) other);
    }

    @Override
    public int[] getSizes() {
        return sizes;
    }

    @Override
    public int getDimensions() {
        return 2;
    }
}
