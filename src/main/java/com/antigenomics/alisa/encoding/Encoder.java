package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.entities.Entity;

import java.util.List;

public interface Encoder<T extends Entity, E extends Encoding> {
    // todo: should have cache

    E encode(final T value);

    E getZero();

    List<T> listPossibleStates();
}
