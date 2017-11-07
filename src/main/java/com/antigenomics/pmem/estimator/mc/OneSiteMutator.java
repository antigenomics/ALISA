package com.antigenomics.pmem.estimator.mc;

import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.state.OneLayerState;

public final class OneSiteMutator<T extends Entity> implements StateMutator<OneLayerState<T>> {
    private final EntityMutator<T> entityMutator;

    public OneSiteMutator(final EntityMutator<T> entityMutator) {
        this.entityMutator = entityMutator;
    }

    public EntityMutator<T> getEntityMutator() {
        return entityMutator;
    }

    @Override
    public OneLayerState<T> mutate(OneLayerState<T> state) {
        return new OneLayerState<>(entityMutator.mutate(state.getValue()));
    }
}
