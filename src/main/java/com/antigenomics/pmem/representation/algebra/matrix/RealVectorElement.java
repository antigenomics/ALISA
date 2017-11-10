package com.antigenomics.pmem.representation.algebra.matrix;

import com.antigenomics.pmem.representation.algebra.Element;

public final class RealVectorElement
        implements Element<RealVectorElement, Double> {
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

    @Override
    public int compareTo(RealVectorElement o) {
        return Integer.compare(index, o.index);
    }

    @Override
    public Double getBoxedValue() {
        return value;
    }
}
