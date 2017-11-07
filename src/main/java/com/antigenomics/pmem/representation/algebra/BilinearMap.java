package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.LinearSpaceObject;

public interface BilinearMap<V extends VectorSpace<V, M>, M extends BilinearMap<V, M>>
        extends LinearSpaceObject<M> {
    double bilinearForm(V a, V b);
}