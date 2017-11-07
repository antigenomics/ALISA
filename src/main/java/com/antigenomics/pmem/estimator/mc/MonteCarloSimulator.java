package com.antigenomics.pmem.estimator.mc;

import com.antigenomics.pmem.state.State;
import com.antigenomics.pmem.hamiltonian.Hamiltonian;
import com.antigenomics.pmem.representation.Representation;

import java.util.ArrayList;

public interface MonteCarloSimulator<S extends State, R extends Representation, H extends Hamiltonian<S, R>> {
    ArrayList<S> simulate(final S state, final R parameters, final H hamiltonian);
}