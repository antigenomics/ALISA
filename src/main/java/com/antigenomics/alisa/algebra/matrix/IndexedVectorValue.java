package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.IndexedValue;

public class IndexedVectorValue
        implements IndexedValue<IndexedVectorValue> {
    public static final IndexedVectorValue EMPTY = new IndexedVectorValue(-1, 0);

    private final int index;
    private final double value;

    public IndexedVectorValue(int index, double value) {
        this.index = index;
        this.value = value;
    }

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

}
