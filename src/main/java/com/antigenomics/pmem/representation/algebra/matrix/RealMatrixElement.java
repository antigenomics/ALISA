package com.antigenomics.pmem.representation.algebra.matrix;

import com.antigenomics.pmem.representation.algebra.Element;

public final class RealMatrixElement
        implements Element<RealMatrixElement, Double> {
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

    @Override
    public Double getBoxedValue() {
        return value;
    }

    @Override
    public int compareTo(RealMatrixElement o) {
        int cmp1 = Integer.compare(index1, o.index1);
        return cmp1 == 0 ? Integer.compare(index2, o.index2) : cmp1;
    }
}
