package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.representation.LinearSpaceObject;
import com.antigenomics.pmem.state.State;

public interface HamiltonianMatrix<S extends State, R extends LinearSpaceObject<R>>
        extends Hamiltonian<S, R> {
}
