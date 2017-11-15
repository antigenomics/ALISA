package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.encoding.State;

public interface MatrixHamiltonian<S extends State,
        M extends LinearSpaceObject<M>>
        extends Hamiltonian<S, LinearSpaceObjectArray<M>> {
}