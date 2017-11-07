package com.antigenomics.pmem.representation.impl;

import com.antigenomics.pmem.representation.LinearSpaceObject;

public abstract class Vector implements LinearSpaceObjectStorage<VectorElement, Vector> {
    protected final int size;

    public Vector(final int size) {
        this.size = size;
    }

    protected abstract Vector plusInner(Vector other);

    @Override
    public LinearSpaceObject<Vector> plus(LinearSpaceObject<Vector> other) {
        assertCompatible((LinearSpaceObjectStorage) other);
        return plusInner((Vector) other);
    }

    @Override
    public int[] getSizes() {
        return new int[]{size};
    }

    @Override
    public int getDimensions() {
        return 1;
    }

    public int getSize() {
        return size;
    }
}
