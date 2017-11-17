package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.IndexedValue;

public class IndexedMatrixValue
        implements IndexedValue<IndexedMatrixValue> {
    public static final IndexedMatrixValue EMPTY = new IndexedMatrixValue(-1, -1, 0);

    private final int rowIndex, colIndex;
    private final double value;

    public IndexedMatrixValue(int rowIndex, int colIndex, double value) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.value = value;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
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
    public IndexedMatrixValue add(IndexedMatrixValue other) {
        return new IndexedMatrixValue(rowIndex, colIndex, value + other.value);
    }

    @Override
    public IndexedMatrixValue scale(double scalar) {
        return new IndexedMatrixValue(rowIndex, colIndex, value * scalar);
    }

    @Override
    public int compareTo(IndexedMatrixValue o) {
        int cmp1 = Integer.compare(rowIndex, o.rowIndex);
        return cmp1 == 0 ? Integer.compare(colIndex, o.colIndex) : cmp1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexedMatrixValue that = (IndexedMatrixValue) o;

        if (rowIndex != that.rowIndex) return false;
        if (colIndex != that.colIndex) return false;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = rowIndex;
        result = 31 * result + colIndex;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
