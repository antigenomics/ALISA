package com.antigenomics.pmem.representation.algebra;

public interface MatrixLinearIndexing {
    int getIndex(int i, int j);

    int getNumberOfRows();

    int getNumberOfColumns();
}
