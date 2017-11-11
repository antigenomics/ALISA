package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.ImmutableLinearSpaceObject;
import com.antigenomics.alisa.representation.algebra.ElementContainer;
import com.antigenomics.alisa.representation.algebra.VectorSpace;

public interface RealVector
        extends ImmutableLinearSpaceObject<RealVector>,
        ElementContainer<RealVectorElement>, VectorSpace<RealVector, RealMatrix> {
    int getSize();

    double getAt(int index);
}
