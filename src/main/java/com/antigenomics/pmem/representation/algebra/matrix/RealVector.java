package com.antigenomics.pmem.representation.algebra.matrix;

import com.antigenomics.pmem.representation.algebra.ElementContainer;
import com.antigenomics.pmem.representation.algebra.VectorSpace;

public interface RealVector
        extends ElementContainer<RealVectorElement>, VectorSpace<RealVector, RealMatrix> {
    int getSize();

    double getAt(int index);
}
