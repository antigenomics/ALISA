package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.representation.LinearSpaceObject;
import com.antigenomics.pmem.representation.LinearSpaceObjectArray;
import com.antigenomics.pmem.state.State;

public interface MatrixHamiltonian<S extends State,
        M extends LinearSpaceObject<M>>
        extends Hamiltonian<S, LinearSpaceObjectArray<M>> {
}
