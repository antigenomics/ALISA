package com.antigenomics.alisa.estimator.mc;

import com.antigenomics.alisa.hamiltonian.Representation;
import com.antigenomics.alisa.encoding.State;
import com.antigenomics.alisa.hamiltonian.Hamiltonian;

import java.util.ArrayList;

public interface MonteCarloSimulator<S extends State, R extends Representation, H extends Hamiltonian<S, R>> {
    ArrayList<S> simulate(final S state, final R parameters, final H hamiltonian);
}