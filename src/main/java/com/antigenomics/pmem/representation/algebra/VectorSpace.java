package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.encoding.EncodingUnit;
import com.antigenomics.pmem.representation.ImmutableLinearSpaceObject;

public interface VectorSpace<V extends VectorSpace<V, M>, M extends BilinearMap<V, M>>
        extends ImmutableLinearSpaceObject<V>, EncodingUnit {
    double dotProduct(V b);

    M outerProduct(V b);

    // outer-product with thyself
    M expand();
}
