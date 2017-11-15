package com.antigenomics.alisa.estimator.mc;

import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.encoding.TwoLayerState;

public final class TwoLayerStateMutator<E1 extends Entity, E2 extends Entity>
        implements StateMutator<TwoLayerState<E1, E2>> {
    private final EntityMutator<E1> firstEntityMutator;
    private final EntityMutator<E2> secondEntityMutator;

    public TwoLayerStateMutator(final EntityMutator<E1> firstEntityMutator,
                                final EntityMutator<E2> secondEntityMutator) {
        this.firstEntityMutator = firstEntityMutator;
        this.secondEntityMutator = secondEntityMutator;
    }

    public EntityMutator<E1> getFirstEntityMutator() {
        return firstEntityMutator;
    }

    public EntityMutator<E2> getSecondEntityMutator() {
        return secondEntityMutator;
    }

    @Override
    public TwoLayerState<E1, E2> mutate(TwoLayerState<E1, E2> state) {
        return new TwoLayerState<>(
                firstEntityMutator.mutate(state.getFirstValue()),
                secondEntityMutator.mutate(state.getSecondValue())
        );
    }
}