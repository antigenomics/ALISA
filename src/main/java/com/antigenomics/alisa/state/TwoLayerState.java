package com.antigenomics.alisa.state;

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
    public double getDegeneracy() {
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

    @Override
    public String toString() {
        return "{" + firstValue + ";" + secondValue + '}' +
                (weight != 1 ? ("^" + (float) weight) : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwoLayerState<?, ?> that = (TwoLayerState<?, ?>) o;

        if (Double.compare(that.weight, weight) != 0) return false;
        if (!firstValue.equals(that.firstValue)) return false;
        return secondValue.equals(that.secondValue);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = firstValue.hashCode();
        result = 31 * result + secondValue.hashCode();
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
