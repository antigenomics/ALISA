package com.antigenomics.pmem.estimator.mc;

import com.antigenomics.pmem.entities.Entity;

public class TwoSiteMutatorS<T extends Entity> extends TwoSiteMutatorNS<T, T> {
    public TwoSiteMutatorS(EntityMutator<T> firstEntityMutator) {
        super(firstEntityMutator, firstEntityMutator);
    }
}
