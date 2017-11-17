package com.antigenomics.alisa.algebra;

/**
 *
 * @param <V>
 * @param <M>
 */
public interface VectorSpace<V extends VectorSpace<V, M>, M extends VectorMapping<V, M>> {
    double dotProduct(V b);

    M outerProduct(V b);

    // outer-product with thyself
    M expand();
}