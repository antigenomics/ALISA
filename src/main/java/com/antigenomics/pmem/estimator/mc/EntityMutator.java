package com.antigenomics.pmem.estimator.mc;

import com.antigenomics.pmem.entities.Entity;

public interface EntityMutator<T extends Entity> {
    T mutate(T entity);
}
