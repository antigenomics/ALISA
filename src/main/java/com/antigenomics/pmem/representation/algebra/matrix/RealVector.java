package com.antigenomics.pmem.representation.algebra.matrix;

import com.antigenomics.pmem.representation.ImmutableLinearSpaceObject;
import com.antigenomics.pmem.representation.algebra.ElementContainer;
import com.antigenomics.pmem.representation.algebra.VectorSpace;

public interface RealVector
        extends ImmutableLinearSpaceObject<RealVector>,
        ElementContainer<RealVectorElement>, VectorSpace<RealVector, RealMatrix> {
    int getSize();

    double getAt(int index);
}
