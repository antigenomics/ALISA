package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.representation.algebra.BilinearMap;
import com.antigenomics.alisa.representation.algebra.VectorSpace;
import com.antigenomics.alisa.state.State;

public interface SpinGlassHamiltonian<S extends State,
        V extends VectorSpace<V, M>,
        M extends BilinearMap<V, M>>
        extends MatrixHamiltonian<S, M> {
}