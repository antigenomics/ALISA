package com.antigenomics.pmem.state;

import com.antigenomics.pmem.entities.Entity;
import com.sun.istack.internal.NotNull;

public final class ThreeLayerState<E1 extends Entity, E2 extends Entity, E3 extends Entity>
        implements State<Entity> {
    private final E1 firstValue;
    private final E2 secondValue;
    private final E3 thirdValue;

    public ThreeLayerState(@NotNull final E1 firstValue,
                           @NotNull final E2 secondValue,
                           @NotNull final E3 thirdValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.thirdValue = thirdValue;
    }

    public E1 getFirstValue() {
        return firstValue;
    }

    public E2 getSecondValue() {
        return secondValue;
    }

    public E3 getThirdValue() {
        return thirdValue;
    }

    @Override
    public Entity getValue(final int layer) {
        switch (layer) {
            case 0:
                return firstValue;
            case 1:
                return secondValue;
            case 2:
                return thirdValue;
        }

        throw new IndexOutOfBoundsException("Layer should equal 0, 1 or 2");
    }

    @Override
    public int getNumberOfLayers() {
        return 3;
    }
}
