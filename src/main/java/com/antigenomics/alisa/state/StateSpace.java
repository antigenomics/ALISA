package com.antigenomics.alisa.state;

public interface StateSpace<S extends State> {
    StateSequence<S> getStateSequence();

    default double getSize() {
        return getStateSequence().getCharacteristicSize();
    }
}
