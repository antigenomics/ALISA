package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.IndexedValue;

public class IndexedTensorValue
        implements IndexedValue<IndexedTensorValue> {
    private final int rowIndex, columnIndex,
            firstCategoryIndex, secondCategoryIndex;
    private final double value;

    public IndexedTensorValue(int rowIndex, int columnIndex,
                              int firstCategoryIndex, int secondCategoryIndex,
                              double value) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.firstCategoryIndex = firstCategoryIndex;
        this.secondCategoryIndex = secondCategoryIndex;
        this.value = value;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getFirstCategoryIndex() {
        return firstCategoryIndex;
    }

    public int getSecondCategoryIndex() {
        return secondCategoryIndex;
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
    public IndexedTensorValue add(IndexedTensorValue other) {
        return new IndexedTensorValue(rowIndex, columnIndex,
                firstCategoryIndex, secondCategoryIndex,
                value + other.value);
    }

    @Override
    public IndexedTensorValue scale(double scalar) {
        return new IndexedTensorValue(rowIndex, columnIndex,
                firstCategoryIndex, secondCategoryIndex,
                value * scalar);
    }

    @Override
    public int compareTo(IndexedTensorValue o) {
        int cmp = Integer.compare(rowIndex, o.rowIndex);

        if (cmp == 0) {
            cmp = Integer.compare(columnIndex, o.columnIndex);
            if (cmp == 0) {
                cmp = Integer.compare(firstCategoryIndex, o.firstCategoryIndex);
                if (cmp == 0) {
                    cmp = Integer.compare(secondCategoryIndex, o.secondCategoryIndex);
                }
            }
        }

        return cmp;
    }
}
