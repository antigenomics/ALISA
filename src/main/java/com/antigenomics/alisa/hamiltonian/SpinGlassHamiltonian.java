package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.ImmutableLinearSpaceObject;
import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.algebra.VectorMapping;
import com.antigenomics.alisa.algebra.VectorSpace;
import com.antigenomics.alisa.encoding.State;

public interface SpinGlassHamiltonian<S extends State,
        V extends VectorSpace<V, M> & ImmutableLinearSpaceObject<M>,
        M extends VectorMapping<V, M> & LinearSpaceObject<M>>
        extends MatrixHamiltonian<S, M> {
}