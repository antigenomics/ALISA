package com.antigenomics.pmem.representation.algebra;

public class RealVectorElement {
    private final int i;
    private final double value;

    public RealVectorElement(int i, double value) {
        this.i = i;
        this.value = value;
    }

    public int getI() {
        return i;
    }

    public double getValue() {
        return value;
    }
}
