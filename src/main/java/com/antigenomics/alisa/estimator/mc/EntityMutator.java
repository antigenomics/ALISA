package com.antigenomics.alisa.estimator.mc;

import com.antigenomics.alisa.entities.Entity;

public interface EntityMutator<T extends Entity> {
    T mutate(T entity);
}
