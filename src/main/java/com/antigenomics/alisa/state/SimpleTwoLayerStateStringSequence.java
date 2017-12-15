package com.antigenomics.alisa.state;

import com.antigenomics.alisa.entities.StateString;
import com.antigenomics.alisa.entities.StateStringSequence;

import java.util.Arrays;

public class SimpleTwoLayerStateStringSequence extends SimpleStateSequence<StateString,
        TwoLayerState<StateString, StateString>> {
    public SimpleTwoLayerStateStringSequence(int numberOfElements,
                                             int arrayLength1, int arrayLength2) {
        super(
                new StateStringSequence(numberOfElements, arrayLength1 + arrayLength2),
                x -> new TwoLayerState<>(
                        getFirst(x, arrayLength1, numberOfElements),
                        getSecond(x, arrayLength1, arrayLength2, numberOfElements)
                )
        );
    }

    private static StateString getFirst(StateString full, int arrayLength1, int numberOfElements) {
        return new StateString(
                Arrays.copyOf(full.toArray(), arrayLength1),
                numberOfElements
        );
    }

    private static StateString getSecond(StateString full, int arrayLength1, int arrayLength2, int numberOfElements) {
        return new StateString(
                Arrays.copyOfRange(full.toArray(), arrayLength1, arrayLength1 + arrayLength2),
                numberOfElements
        );
    }
}
