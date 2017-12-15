package com.antigenomics.alisa.state;

import com.antigenomics.alisa.entities.StateString;
import com.antigenomics.alisa.entities.StateStringSequence;

public class SimpleOneLayerStateStringSequence extends SimpleStateSequence<StateString, OneLayerState<StateString>> {
    public SimpleOneLayerStateStringSequence(int numberOfElements, int arrayLength) {
        super(new StateStringSequence(numberOfElements, arrayLength),
                OneLayerState::new);
    }
}
