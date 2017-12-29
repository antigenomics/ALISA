package com.antigenomics.alisa.state;

import com.antigenomics.alisa.entities.Entity;

public final class OneLayerState<E extends Entity>
        implements State<E> {
    private final double weight;
    private final E value;

    public OneLayerState(final E value) {
        this(value, 1.0);
    }

    public OneLayerState(final E value,
                         double weight) {
        this.value = value;
        this.weight = weight;
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

    @Override
    public double getDegeneracy() {
        return weight;
    }

    @Override
    public String toString() {
        return "{" + value + '}' + (weight != 1 ? ("^" + weight) : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OneLayerState<?> that = (OneLayerState<?>) o;

        if (Double.compare(that.weight, weight) != 0) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(weight);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + value.hashCode();
        return result;
    }
}
