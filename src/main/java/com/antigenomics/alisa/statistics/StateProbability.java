package com.antigenomics.alisa.statistics;

import com.antigenomics.alisa.state.State;

public final class StateProbability<S extends State> {
    private final S state;
    private final double probability;

    public StateProbability(S state, double probability) {
        this.state = state;
        this.probability = probability;
    }

    public S getState() {
        return state;
    }

    public double getProbability() {
        return probability;
    }

    public double getFullProbability() {
        return probability * state.getDegeneracy();
    }
}
