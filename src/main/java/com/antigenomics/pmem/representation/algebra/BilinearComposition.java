package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.LinearSpaceObject;

public interface BilinearComposition<V extends VectorSpace<V, M>, M extends BilinearComposition<V, M>>
        extends LinearSpaceObject<M> {
    double bilinearForm(V a, V b);
}