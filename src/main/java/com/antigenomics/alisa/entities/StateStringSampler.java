package com.antigenomics.alisa.entities;

import com.antigenomics.alisa.RandomSampler;
import com.antigenomics.alisa.estimator.mc.MonteCarloUtils;

public final class StateStringSampler implements RandomSampler<StateString> {
    private final int numberOfElements, arrayLength;

    public StateStringSampler(int numberOfElements, int arrayLength) {
        this.numberOfElements = numberOfElements;
        this.arrayLength = arrayLength;
    }

    @Override
    public StateString sample() {
        return new StateString(MonteCarloUtils.nextIntArray(numberOfElements, arrayLength),
                numberOfElements);
    }
}
