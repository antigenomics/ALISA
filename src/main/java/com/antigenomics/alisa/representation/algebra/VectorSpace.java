package com.antigenomics.alisa.representation.algebra;

import com.antigenomics.alisa.encoding.EncodingUnit;

public interface VectorSpace<V extends VectorSpace<V, M>, M extends BilinearMap<V, M>>
        extends EncodingUnit {
    double dotProduct(V b);

    M outerProduct(V b);

    // outer-product with thyself
    M expand();
}
