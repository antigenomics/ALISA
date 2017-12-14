package com.antigenomics.alisa.estimator.mc;

import com.antigenomics.alisa.entities.StateString;
import com.antigenomics.alisa.estimator.mc.EntityMutator;
import com.antigenomics.alisa.estimator.mc.MonteCarloUtils;

public class StateStringMutator implements EntityMutator<StateString> {
    @Override
    public StateString mutate(StateString entity) {
        final int[] states = entity.toArray();
        final int mutationPos = MonteCarloUtils.nextInt(states.length);

        if (entity.getNumberOfStates() == 2) {
            states[mutationPos] = 1 - states[mutationPos];
        } else {
            int mutatedState;

            // Force replacement mutation, there is 1/20 chance that we'll change to the same AA
            // Its far better to waste time here than computing energy for the same AA sequence in MC
            while ((mutatedState = MonteCarloUtils.nextInt(entity.getNumberOfStates())) == states[mutationPos]) ;

            states[mutationPos] = mutatedState;
        }

        return new StateString(states, entity.getNumberOfStates());
    }
}
