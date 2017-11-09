package com.antigenomics.pmem.representation.algebra;

public class RealMatrixElement {
    private final int index1, index2;
    private final double value;

    public RealMatrixElement(int index1, int index2, double value) {
        this.index1 = index1;
        this.index2 = index2;
        this.value = value;
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    public double getValue() {
        return value;
    }
}
