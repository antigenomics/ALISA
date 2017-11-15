package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.entities.Entity;
import com.sun.istack.internal.NotNull;

public final class OneLayerState<E extends Entity>
        implements State<E> {
    private final E value;

    public OneLayerState(@NotNull final E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }

    @Override
    public E getValue(final int layer) {
        if (layer != 0) {
            throw new IndexOutOfBoundsException("Layer should equal 0");
        }
        return getValue();
    }

    @Override
    public int getNumberOfLayers() {
        return 1;
    }
}
