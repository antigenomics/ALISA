package com.antigenomics.alisa.representation.algebra.matrix;

public interface MatrixShape {
    int getNumberOfRows();

    int getNumberOfColumns();

    default boolean isSquare() {
        return getNumberOfRows() == getNumberOfColumns();
    }

    boolean isStrictlySymmetric();
}
