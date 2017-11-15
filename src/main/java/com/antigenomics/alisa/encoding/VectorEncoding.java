package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.algebra.ImmutableLinearSpaceObject;
import com.antigenomics.alisa.hamiltonian.MatrixRepresentation;
import com.antigenomics.alisa.algebra.VectorSpace;

public interface VectorEncoding<V extends VectorEncoding<V, M>, M extends MatrixRepresentation<V, M>>
        extends VectorSpace<V, M>, ImmutableLinearSpaceObject<M> {
}
