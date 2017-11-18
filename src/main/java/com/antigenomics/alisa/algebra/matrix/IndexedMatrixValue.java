package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.IndexedValue;

/**
 * An indexed matrix element. Contains real value, row and column indices.
 * The comparison operation first compares the row index, the the column index, corresponding
 * to linear indexing of an array with first row followed by second row and so on.
 */
public class IndexedMatrixValue
        implements IndexedValue<IndexedMatrixValue> {
    /**
     * An empty value, indices are set to -1 and value is set to zero.
     */
    public static final IndexedMatrixValue EMPTY = new IndexedMatrixValue(-1, -1, 0);

    private final int rowIndex, colIndex;
    private final double value;

    /**
     * Create a new indexed matrix value.
     *
     * @param rowIndex row index
     * @param colIndex column index
     * @param value    real value
     */
    public IndexedMatrixValue(int rowIndex, int colIndex, double value) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.value = value;
    }

    /**
     * Get row index of the element.
     *
     * @return row index
     */
    public int getRowIndex() {
        return rowIndex;
    }


    /**
     * Get column index of the element.
     *
     * @return row column
     */
    public int getColIndex() {
        return colIndex;
    }

    /**
     * Get the real value of the element.
     *
     * @return double value
     */
    @Override
    public double getDoubleValue() {
        return value;
    }

    /**
     * Get the real value of the element and convert it to integer.
     *
     * @return integer value
     */
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

