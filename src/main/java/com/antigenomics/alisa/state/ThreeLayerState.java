package com.antigenomics.alisa.state;

import com.antigenomics.alisa.entities.Entity;

public final class ThreeLayerState<E1 extends Entity, E2 extends Entity, E3 extends Entity>
        implements State<Entity> {
    private final E1 firstValue;
    private final E2 secondValue;
    private final E3 thirdValue;
    private final double weight;

    public ThreeLayerState(final E1 firstValue,
                           final E2 secondValue,
                           final E3 thirdValue) {
        this(firstValue, secondValue, thirdValue, 1.0);
    }

    public ThreeLayerState(final E1 firstValue,
                           final E2 secondValue,
                           final E3 thirdValue,
                           double weight) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.thirdValue = thirdValue;
        this.weight = weight;
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
            case 2:
                return thirdValue;
        }

        throw new IndexOutOfBoundsException("Layer should equal 0, 1 or 2");
    }

    @Override
    public int getNumberOfLayers() {
        return 3;
    }


    @Override
    public String toString() {
        return "{" + firstValue + ";" + secondValue + ";" + thirdValue + '}' + (weight != 1 ? ("^" + weight) : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThreeLayerState<?, ?, ?> that = (ThreeLayerState<?, ?, ?>) o;

        if (Double.compare(that.weight, weight) != 0) return false;
        if (!firstValue.equals(that.firstValue)) return false;
        if (!secondValue.equals(that.secondValue)) return false;
        return thirdValue.equals(that.thirdValue);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = firstValue.hashCode();
        result = 31 * result + secondValue.hashCode();
        result = 31 * result + thirdValue.hashCode();
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
