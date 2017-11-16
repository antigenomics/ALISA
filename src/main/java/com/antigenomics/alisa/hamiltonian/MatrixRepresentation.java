package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.BilinearMap;
import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.encoding.VectorEncoding;

public interface MatrixRepresentation<V extends VectorEncoding<V, M>, M extends MatrixRepresentation<V, M>>
        extends BilinearMap<V, M>, LinearSpaceObject<M> {
}