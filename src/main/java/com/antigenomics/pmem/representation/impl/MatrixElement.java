package com.antigenomics.pmem.representation.impl;

public final class MatrixElement implements LinearSpaceObjectElement {
    private final double value;
    private final int i, j;

    public MatrixElement(double value, int i, int j) {
        this.value = value;
        this.i = i;
        this.j = j;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public int getIndex(int dimension) {
        if (dimension == 0) {
            return i;
        } else if (dimension == 1) {
            return j;
        }
        throw new IndexOutOfBoundsException();
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
