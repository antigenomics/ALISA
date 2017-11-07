package com.antigenomics.pmem.estimator.mc;

import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.state.TwoSiteStateNS;

public class TwoSiteMutatorNS<T extends Entity, U extends Entity> implements StateMutator<TwoSiteStateNS<T, U>> {
    private final EntityMutator<T> firstEntityMutator;
    private final EntityMutator<U> secondEntityMutator;

    public TwoSiteMutatorNS(final EntityMutator<T> firstEntityMutator,
                            final EntityMutator<U> secondEntityMutator) {
        this.firstEntityMutator = firstEntityMutator;
        this.secondEntityMutator = secondEntityMutator;
    }

    public EntityMutator<T> getFirstEntityMutator() {
        return firstEntityMutator;
    }

    public EntityMutator<U> getSecondEntityMutator() {
        return secondEntityMutator;
    }

    @Override
    public TwoSiteStateNS<T, U> mutate(TwoSiteStateNS<T, U> state) {
        return new TwoSiteStateNS<>(firstEntityMutator.mutate(state.getFirstValue()),
                secondEntityMutator.mutate(state.getSecondValue()));
    }
}