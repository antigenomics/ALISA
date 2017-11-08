package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.representation.ImmutableLinearSpaceObject;
import com.antigenomics.pmem.representation.ImmutableLSOArray;
import com.antigenomics.pmem.state.State;

public interface MatrixHamiltonian<S extends State,
        M extends ImmutableLinearSpaceObject<M>>
        extends Hamiltonian<S, ImmutableLSOArray<M>> {
}
