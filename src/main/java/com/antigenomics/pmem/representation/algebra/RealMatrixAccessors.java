package com.antigenomics.pmem.representation.algebra;

public interface RealMatrixAccessors {
    int getSize1();

    int getSize2();

    double getAt(int i, int j);

    default boolean isSquare() {
        return getSize1() == getSize2();
    }

    boolean isStrictlySymmetric();
}
