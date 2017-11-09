package com.antigenomics.pmem.representation.algebra;

public interface RealVector
        extends ElementContainer<RealVectorElement>, VectorSpace<RealVector, RealMatrix> {
    int getSize();

    double getAt(int i);
}
