package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.state.State;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.stream.StreamSupport;

public interface Hamiltonian<S extends State, R extends Representation> {
    double computeEnergy(final S state, final R parameters);

    R computeGradient(final S state, final R parameters);

    R getZeroParameters();

    // this method should always copy
    R createParameterGuess();

    default boolean hasTheoreticalPartitionFunction() {
        return false;
    }

    default double computePartitionFunction(R parameters) {
        throw new NotImplementedException();
    }
}