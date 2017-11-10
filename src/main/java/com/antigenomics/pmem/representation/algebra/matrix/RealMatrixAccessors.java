package com.antigenomics.pmem.representation.algebra.matrix;

public interface RealMatrixAccessors {
    int getNumberOfRows();

    int getNumberOfColumns();

    double getAt(int i, int j);

    default boolean isSquare() {
        return getNumberOfRows() == getNumberOfColumns();
    }

    boolean isStrictlySymmetric();
}
