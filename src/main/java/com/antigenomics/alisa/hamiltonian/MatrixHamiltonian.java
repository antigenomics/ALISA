package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.algebra.matrix.Matrix;
import com.antigenomics.alisa.state.State;

import java.util.Arrays;

public interface MatrixHamiltonian<S extends State,
        M extends LinearSpaceObject<M>>
        extends Hamiltonian<S, LinearSpaceObjectArray<M>> {
    @SuppressWarnings("unchecked")
    static LinearSpaceObjectArray asParameters(Matrix... matrices) {
        return new LinearSpaceObjectArray(Arrays.asList(matrices));
    }

    @Override
    default LinearSpaceObjectArray<M> createParameterGuess() {
        return getZeroParameters().asDense();
    }
}