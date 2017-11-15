package com.antigenomics.alisa.estimator.mc;

import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.encoding.ThreeLayerState;

public final class ThreeLayerStateMutator<E1 extends Entity, E2 extends Entity, E3 extends Entity>
        implements StateMutator<ThreeLayerState<E1, E2, E3>> {
    private final EntityMutator<E1> firstEntityMutator;
    private final EntityMutator<E2> secondEntityMutator;
    private final EntityMutator<E3> thirdEntityMutator;

    public ThreeLayerStateMutator(final EntityMutator<E1> firstEntityMutator,
                                  final EntityMutator<E2> secondEntityMutator,
                                  final EntityMutator<E3> thirdEntityMutator) {
        this.firstEntityMutator = firstEntityMutator;
        this.secondEntityMutator = secondEntityMutator;
        this.thirdEntityMutator = thirdEntityMutator;
    }

    public EntityMutator<E1> getFirstEntityMutator() {
        return firstEntityMutator;
    }

    public EntityMutator<E2> getSecondEntityMutator() {
        return secondEntityMutator;
    }

    public EntityMutator<E3> getThirdEntityMutator() {
        return thirdEntityMutator;
    }

    @Override
    public ThreeLayerState<E1, E2, E3> mutate(ThreeLayerState<E1, E2, E3> state) {
        return new ThreeLayerState<>(
                firstEntityMutator.mutate(state.getFirstValue()),
                secondEntityMutator.mutate(state.getSecondValue()),
                thirdEntityMutator.mutate(state.getThirdValue())
        );
    }
}