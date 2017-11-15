package com.antigenomics.alisa.estimator.mc;

import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.encoding.OneLayerState;

public final class OneLayerStateMutator<E extends Entity>
        implements StateMutator<OneLayerState<E>> {
    private final EntityMutator<E> entityMutator;

    public OneLayerStateMutator(final EntityMutator<E> entityMutator) {
        this.entityMutator = entityMutator;
    }

    public EntityMutator<E> getEntityMutator() {
        return entityMutator;
    }

    @Override
    public OneLayerState<E> mutate(OneLayerState<E> state) {
        return new OneLayerState<>(entityMutator.mutate(state.getValue()));
    }
}
