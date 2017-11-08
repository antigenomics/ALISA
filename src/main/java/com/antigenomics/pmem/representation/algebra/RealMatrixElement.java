package com.antigenomics.pmem.representation.algebra;

public class RealMatrixElement {
    private final int i, j;
    private final double value;

    public RealMatrixElement(int i, int j, double value) {
        this.i = i;
        this.j = j;
        this.value = value;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public double getValue() {
        return value;
    }
}
