package com.antigenomics.pmem.state;

import com.antigenomics.pmem.entities.Entity;
import com.sun.istack.internal.NotNull;

public class OneSiteState<T extends Entity> implements State<T> {
    private final T value;

    public OneSiteState(@NotNull final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public T getValue(final int site) {
        if (site != 0) {
            throw new IllegalArgumentException("Site should equal 0");
        }
        return getValue();
    }

    @Override
    public int getNumberOfSites() {
        return 1;
    }
}
