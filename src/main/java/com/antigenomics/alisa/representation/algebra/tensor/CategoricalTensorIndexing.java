package com.antigenomics.alisa.representation.algebra.tensor;

public interface CategoricalTensorIndexing
        extends CategoricalTensorShape {
    int getIndex(int a, int b, int i, int j);

    int getEffectiveSize();
}
