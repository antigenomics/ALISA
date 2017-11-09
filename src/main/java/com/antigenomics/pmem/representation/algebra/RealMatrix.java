package com.antigenomics.pmem.representation.algebra;

public interface RealMatrix
        extends ElementContainer<RealMatrixElement>, BilinearMap<RealVector, RealMatrix> {
    int getSize1();

    int getSize2();

    int getAt(int i, int j);
}
