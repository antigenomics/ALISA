package com.antigenomics.alisa.state;

import com.antigenomics.alisa.SequenceGenerator;

public interface StateSequence<S extends State> extends SequenceGenerator<S> {
    StateSpace<S> asStateSpace();
}
