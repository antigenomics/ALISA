package com.antigenomics.pmem.representation.algebra;

public class DenseMatrix {
    private final double[] innerArray;
    private final int numberOfColumns,
            numberOfRows;

    public DenseMatrix(double[] innerArray,
                       int numberOfColumns) {
        this.innerArray = innerArray;
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = innerArray.length / numberOfColumns;
    }

    protected int getIndex(int i, int j) {
        return i * numberOfColumns + j;
    }

    public double getAt(int i, int j) {
        return innerArray[getIndex(i, j)];
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }
}
