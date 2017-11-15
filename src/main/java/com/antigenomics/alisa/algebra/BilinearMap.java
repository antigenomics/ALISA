package com.antigenomics.alisa.algebra;

public interface BilinearMap<V extends VectorSpace<V, M>, M extends BilinearMap<V, M>> {
    double bilinearForm(V a, V b);

    double bilinearForm(V a);

    V linearForm(V a);
}