package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.entities.Entity;

public final class TwoLayerState<E1 extends Entity, E2 extends Entity>
        implements State<Entity> {
    private final E1 firstValue;
    private final E2 secondValue;

    public TwoLayerState(final E1 firstValue, final E2 secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public E1 getFirstValue() {
        return firstValue;
    }

    public E2 getSecondValue() {
        return secondValue;
    }

    @Override
    public Entity getValue(final int layer) {
        switch (layer) {
            case 0:
                return firstValue;
            case 1:
                return secondValue;
        }

        throw new IndexOutOfBoundsException("Layer should equal 0 or 1");
    }

    @Override
    public int getNumberOfLayers() {
        return 2;
    }
}
