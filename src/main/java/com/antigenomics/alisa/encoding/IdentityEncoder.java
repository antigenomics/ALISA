package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.entities.Entity;
import com.sun.istack.internal.NotNull;

public abstract class IdentityEncoder<T extends Entity & EncodingUnit> implements Encoder<T, T> {
    public T encode(@NotNull final T value) {
        return value;
    }
}
