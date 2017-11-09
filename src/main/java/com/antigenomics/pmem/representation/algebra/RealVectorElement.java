package com.antigenomics.pmem.representation.algebra;

public final class RealVectorElement {
    private final int index;
    private final double value;

    public RealVectorElement(int index, double value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public double getValue() {
        return value;
    }
}
