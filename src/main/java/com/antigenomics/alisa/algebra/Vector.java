package com.antigenomics.alisa.algebra;

import com.sun.istack.internal.NotNull;

public abstract class Vector
        implements LinearSpaceObject<Vector>, VectorSpace<Vector, Matrix>,
        Container<IndexedVectorValue, Vector> {
    protected final int length;

    public Vector(int length) {
        this.length = length;
    }

    protected abstract Vector addUnchecked(@NotNull final Vector other);

    protected abstract void addInplaceUnchecked(@NotNull final Vector other);

    protected abstract double dotProductUnchecked(@NotNull final Vector b);

    public int getLength() {
        return length;
    }

    @Override
    public Vector add(@NotNull final Vector other) {
        checkSizeMatch(other);
        return addUnchecked(other);
    }

    @Override
    public void addInplace(@NotNull final Vector other) {
        checkSizeMatch(other);
        addInplaceUnchecked(other);
    }

    @Override
    public double dotProduct(@NotNull final Vector b) {
        checkSizeMatch(b);
        return dotProductUnchecked(b);
    }

    private void checkSizeMatch(@NotNull final Vector other) {
        if (length != other.length)
            throw new IllegalArgumentException("Vector lengths don't match");
    }

    public abstract double getAt(int index);

    @Override
    public double getAt(int... indices) {
        if (indices.length > 1)
            throw new IllegalArgumentException();
        return getAt(indices[0]);
    }

    @Override
    public double norm1() {
        double norm1 = 0;

        if (isSparse()) {
            for (IndexedVectorValue x : this) {
                norm1 += Math.abs(x.getDoubleValue());
            }
        } else {
            for (int i = 0; i < length; i++) {
                norm1 += Math.abs(getAt(i));
            }
        }

        return norm1;
    }

    @Override
    public double norm2() {
        double norm2 = 0;

        if (isSparse()) {
            for (IndexedVectorValue x : this) {
                norm2 += x.getDoubleValue() * x.getDoubleValue();
            }
        } else {
            for (int i = 0; i < length; i++) {
                double value = getAt(i);
                norm2 += value * value;
            }
        }

        return norm2;
    }
}
