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

    @Override
    public String toString() {
        return "P(" + state + ")=" + probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateProbability<?> that = (StateProbability<?>) o;

        if (Double.compare(that.probability, probability) != 0) return false;
        return state.equals(that.state);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = state.hashCode();
        temp = Double.doubleToLongBits(probability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
