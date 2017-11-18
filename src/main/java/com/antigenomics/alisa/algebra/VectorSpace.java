package com.antigenomics.alisa.algebra;

/**
 *
 * @param <V> vector space type
 * @param <M> cognate mapping type
 */
public interface VectorSpace<V extends VectorSpace<V, M>, M extends VectorMapping<V, M>> {
    double dotProduct(V b);

    M outerProduct(V b);

    // outer-product with thyself
    M expand();
}