package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.entities.EntityGenerator;

public interface Encoder<T extends Entity, E extends Encoding> {
    // todo: should have cache

    E encode(final T value);

    E getZero();

    EntityGenerator<T> getGenerator();
}
