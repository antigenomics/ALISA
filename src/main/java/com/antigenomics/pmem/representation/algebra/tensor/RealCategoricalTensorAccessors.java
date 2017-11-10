package com.antigenomics.pmem.representation.algebra.tensor;

public interface RealCategoricalTensorAccessors {
    int getNumberOfIndices1();

    int getNumberOfIndices2();

    int getNumberOfCategories1();

    int getNumberOfCategories2();

    double getAt(int i, int j, int a, int b);

    default boolean isSquareByIndices() {
        return getNumberOfIndices1() == getNumberOfIndices2();
    }

    default boolean isSquareByCategories() {
        return getNumberOfCategories1() == getNumberOfCategories2();
    }

    boolean isStrictlySymmetricByIndices();

    boolean isStrictlySymmetricByCategories();
}
