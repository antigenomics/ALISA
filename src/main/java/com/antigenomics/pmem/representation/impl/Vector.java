package com.antigenomics.pmem.representation.impl;

public abstract class Vector implements LinearSpaceObjectStorage<VectorElement, Vector> {
    protected final int size;

    public Vector(final int size) {
        this.size = size;
    }

    public abstract int getEffectiveSize();

    protected abstract Vector plusInner(Vector other);

    public abstract double getAt(int index);

    @Override
    public int[] getEffectiveSizes() {
        return new int[]{getEffectiveSize()};
    }

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

    @Override
    public double getAt(int[] indices) {
        if (indices.length != 1) {
            throw new IllegalArgumentException("One index should be provided");
        }
        int index = indices[0];
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        return getAt(index);
    }

    public int getSize() {
        return size;
    }

    public void assertVectorCompatible(Vector other) {
        if (this.size != other.size) {
            throw new IllegalArgumentException("Vector sizes don't match.");
        }
    }

    public abstract double[] asArray();
}