package com.antigenomics.pmem.representation.algebra.tensor;

public interface CategoricalTensorShape {
    default boolean isSquare() {
        return isSquareByCategories() && getNumberOfIndices1() == getNumberOfIndices2();
    }

    default boolean isSquareByCategories() {
        return getNumberOfCategories1() == getNumberOfCategories2();
    }

    boolean isStrictlySymmetric();

    boolean isStrictlySymmetricByCategories();

    int getNumberOfCategories1();

    int getNumberOfCategories2();

    int getNumberOfIndices1();

    int getNumberOfIndices2();
}
