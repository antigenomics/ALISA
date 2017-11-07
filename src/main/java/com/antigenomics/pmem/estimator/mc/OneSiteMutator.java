package com.antigenomics.pmem.estimator.mc;

import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.state.OneSiteState;

public final class OneSiteMutator<T extends Entity> implements StateMutator<OneSiteState<T>> {
    private final EntityMutator<T> entityMutator;

    public OneSiteMutator(final EntityMutator<T> entityMutator) {
        this.entityMutator = entityMutator;
    }

    public EntityMutator<T> getEntityMutator() {
        return entityMutator;
    }

    @Override
    public OneSiteState<T> mutate(OneSiteState<T> state) {
        return new OneSiteState<>(entityMutator.mutate(state.getValue()));
    }
}
