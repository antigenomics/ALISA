package com.antigenomics.alisa.algebra;

public interface VectorSpace<V extends VectorSpace<V, M>, M extends BilinearMap<V, M>> {
    double dotProduct(V b);

    M outerProduct(V b);

    // outer-product with thyself
    M expand();
}