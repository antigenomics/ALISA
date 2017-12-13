package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.entities.Entity;

public final class TwoLayerState<E1 extends Entity, E2 extends Entity>
        implements State<Entity> {
    private final E1 firstValue;
    private final E2 secondValue;
    private final double weight;

    public TwoLayerState(final E1 firstValue,
                         final E2 secondValue) {
        this(firstValue, secondValue, 1.0);
    }

    public TwoLayerState(final E1 firstValue,
                         final E2 secondValue,
                         double weight) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.weight = weight;
    }

    public E1 getFirstValue() {
        return firstValue;
    }

    public E2 getSecondValue() {
        return secondValue;
    }

    @Override
    public double getWeight() {
        return weight;
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
