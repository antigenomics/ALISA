package com.antigenomics.alisa.state;

public class DummyStateSpace<S extends State> implements StateSpace<S> {
    private final StateSequence<S> stateSequence;

    public DummyStateSpace(StateSequence<S> stateSequence) {
        this.stateSequence = stateSequence;
    }

    @Override
    public StateSequence<S> getStateSequence() {
        return stateSequence;
    }
}
