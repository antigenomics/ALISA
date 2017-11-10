package com.antigenomics.pmem.representation.algebra.matrix;

public abstract class SafeRealVector
        implements RealVector {
    protected abstract double dotProductUnchecked(RealVector b);

    @Override
    public final double dotProduct(RealVector b) {
        if (isCompatible(b)) {
            return dotProductUnchecked(b);
        }
        throw new IllegalArgumentException("Vector " + this + " is not compatible with " + b);
    }

    protected abstract RealVector plusUnchecked(RealVector other);

    @Override
    public final RealVector plus(RealVector other) {
        if (isCompatible(other)) {
            return plusUnchecked(other);
        }
        throw new IllegalArgumentException("Vector " + this + " is not compatible with " + other);
    }

    private boolean isCompatible(RealVector other) {
        return this.getSize() == other.getSize();
    }

    @Override
    public RealMatrix expand() {
        return outerProduct(this);
    }
}
