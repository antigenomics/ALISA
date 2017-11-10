package com.antigenomics.pmem.representation.algebra.tensor;

public interface IndexVector {
    int getSize();

    int getRange();

    int getAt(int i);
}
