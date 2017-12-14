package com.antigenomics.alisa.entities;

import java.util.Arrays;

public class StateString implements Entity {
    public static final StateString DUMMY = new StateString(new int[0], 0);

    protected final int[] states;
    private final int numberOfStates;

    public StateString(int[] states, int numberOfStates) {
        this.states = states;
        this.numberOfStates = numberOfStates;
    }

    public int getLength() {
        return states.length;
    }

    public int getAt(int pos) {
        return states[pos];
    }

    public int[] toArray() {
        return Arrays.copyOf(states, states.length);
    }

    public int getNumberOfStates() {
        return numberOfStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateString that = (StateString) o;

        if (numberOfStates != that.numberOfStates) return false;
        return Arrays.equals(states, that.states);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(states);
        result = 31 * result + numberOfStates;
        return result;
    }
}
