package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.IndexedValue;

/**
 * An indexed vector element. Contains real value and its indexs.
 * The comparison operation first compares the index.
 */
public final class IndexedVectorValue
        implements IndexedValue<IndexedVectorValue> {
    public static final IndexedVectorValue EMPTY = new IndexedVectorValue(-1, 0);

    private final int index;
    private final double value;

    /**
     * Creates a new indexed vector value.
     * @param index position in vector
     * @param value element value
     */
    public IndexedVectorValue(int index, double value) {
        this.index = index;
        this.value = value;
    }

    public IndexedVectorValue(int index) {
        this(index, 1.0);
    }

    /**
     * Gets the position in vector
     * @return index
     */
    public int getIndex() {
        return index;
    }

    @Override
    public double getDoubleValue() {
        return value;
    }

    @Override
    public int getIntValue() {
        return (int) value;
    }

    @Override
    public IndexedVectorValue add(IndexedVectorValue other) {
        return new IndexedVectorValue(index, value + other.value);
    }

    @Override
    public IndexedVectorValue scale(double scalar) {
        return new IndexedVectorValue(index, value * scalar);
    }

    @Override
    public int compareTo(IndexedVectorValue o) {
        return Integer.compare(index, o.index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexedVectorValue that = (IndexedVectorValue) o;

        if (index != that.index) return false;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = index;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
