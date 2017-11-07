package com.antigenomics.pmem.representation.impl;

public final class VectorElement implements LinearSpaceObjectElement {
    private final double value;
    private final int index;

    public VectorElement(double value, int index) {
        this.value = value;
        this.index = index;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public int getIndex(int dimension) {
        if (dimension != 0) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }

    public int getIndex() {
        return index;
    }
}
