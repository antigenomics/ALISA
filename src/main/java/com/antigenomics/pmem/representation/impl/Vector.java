package com.antigenomics.pmem.representation.impl;

public abstract class Vector implements LinearSpaceObjectStorage<VectorElement, Vector> {
    protected final int size;

    public Vector(final int size) {
        this.size = size;
    }

    protected abstract Vector plusInner(Vector other);

    @Override
    public Vector plus(Vector other) {
        assertCompatible(other);
        return plusInner(other);
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
