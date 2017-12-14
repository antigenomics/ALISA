package com.antigenomics.alisa.statistics;

import com.antigenomics.alisa.state.State;

public interface StateSpace<S extends State> {
    StateGenerator<S> getStateGenerator();

    default double getSize() {
        return getStateGenerator().getCharacteristicSize();
    }
}
