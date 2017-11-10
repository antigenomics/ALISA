package com.antigenomics.pmem.representation.algebra;

public interface MatrixStorageIndex {
    int getIndex(int i, int j);

    int getNumberOfRows();

    int getNumberOfColumns();
}
