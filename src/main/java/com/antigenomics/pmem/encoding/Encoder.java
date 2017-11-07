package com.antigenomics.pmem.encoding;

import com.antigenomics.pmem.entities.Entity;
import com.sun.istack.internal.NotNull;

public interface Encoder<T extends Entity, E extends EncodingUnit> {
    E encode(@NotNull final T value);

    E getZero();
}
