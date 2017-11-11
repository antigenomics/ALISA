package com.antigenomics.pmem.representation.algebra.tensor;

public interface RealCategoricalTensorAccessor
        extends CategoricalTensorShape {
    double getAt(int a, int b, int i, int j);
}
