package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.ImmutableLinearSpaceObject;

public interface BilinearMap<V extends VectorSpace<V, M>, M extends BilinearMap<V, M>>
        extends ImmutableLinearSpaceObject<M> {
    double bilinearForm(V a, V b);

    double bilinearForm(V a);
}