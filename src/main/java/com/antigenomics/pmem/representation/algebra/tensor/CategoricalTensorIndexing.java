package com.antigenomics.pmem.representation.algebra.tensor;

public interface CategoricalTensorIndexing
        extends CategoricalTensorShape {
    int getIndex(int a, int b, int i, int j);
}
