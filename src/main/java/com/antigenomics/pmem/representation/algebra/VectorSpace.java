package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.encoding.EncodingUnit;
import com.antigenomics.pmem.representation.LinearSpaceObject;

public interface VectorSpace<V extends VectorSpace<V, M>, M extends BilinearComposition<V, M>>
        extends LinearSpaceObject<V>, EncodingUnit {
    double dotProduct(V b);

    M outerProduct(V b);

    // self cross-product
    default M expand() {
        return outerProduct((V) this);
    }
}
