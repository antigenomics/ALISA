package com.antigenomics.pmem.state;

import com.antigenomics.pmem.entities.Entity;
import com.sun.istack.internal.NotNull;

public class TwoSiteStateNS<T extends Entity, U extends Entity> implements State<Entity> {
    private final T firstValue;
    private final U secondValue;

    public TwoSiteStateNS(@NotNull final T firstValue, @NotNull final U secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public T getFirstValue() {
        return firstValue;
    }

    public U getSecondValue() {
        return secondValue;
    }

    @Override
    public Entity getValue(final int site) {
        if (site == 0) {
            return getFirstValue();
        } else if (site == 1) {
            return getSecondValue();
        }
        throw new IllegalArgumentException("Site should equal 0");
    }

    @Override
    public int getNumberOfSites() {
        return 2;
    }
}
