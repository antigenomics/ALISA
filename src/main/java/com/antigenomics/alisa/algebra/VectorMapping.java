package com.antigenomics.alisa.algebra;

/**
 * A mapping from a vector space to itself (e.g. a matrix-vector product, $A x = y$) or
 * a scalar (a bilinear form, $x^T A y = scalar$).
 *
 * @param <V> cognate vector space type
 * @param <M> mapping type
 */
public interface VectorMapping<V extends VectorSpace<V, M>, M extends VectorMapping<V, M>> {
    /**
     * Computes a bilinear form for a pair of vector space objects.
     * The mapping for 2D case can be written as $\sum_{ij} A_{ij} a_i b_j$,
     * where $A_{ij}$ is the matrix representation of this mapping.
     *
     * @param a first vector
     * @param b second vector
     * @return scalar value
     */
    double bilinearForm(V a, V b);

    /**
     * Computes a symmetric bilinear form for a vector space object.
     * The mapping for 2D case can be written as $\sum_{ij} A_{ij} a_i a_j$,
     * where $A_{ij}$ is the matrix representation of this mapping.
     *
     * @param a an input vector
     * @return scalar value
     */
    double bilinearForm(V a);

    /**
     * Maps one vector object to another.
     * The mapping for 2D case can be written as $\sum_{j} A_{ij} a_j$,
     * where $A_{ij}$ is the matrix representation of this mapping.
     *
     * @param a an input vector
     * @return resulting vector
     */
    V map(V a);
}