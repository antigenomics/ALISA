package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.representation.ImmutableLinearSpaceObject;
import com.antigenomics.alisa.representation.ImmutableLSOArray;
import com.antigenomics.alisa.state.State;

public interface MatrixHamiltonian<S extends State,
        M extends ImmutableLinearSpaceObject<M>>
        extends Hamiltonian<S, ImmutableLSOArray<M>> {
}
