package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.encoding.State;
import com.antigenomics.alisa.encoding.VectorEncoding;

public interface SpinGlassHamiltonian<S extends State,
        V extends VectorEncoding<V, M>,
        M extends MatrixRepresentation<V, M>>
        extends MatrixHamiltonian<S, M> {
}