package com.antigenomics.alisa.representation.algebra.tensor;

public class FullCategoricalTensorIndexing
        implements CategoricalTensorIndexing {
    private final int numberOfCategories1,
            numberOfCategories2,
            numberOfIndices1,
            numberOfIndices2;

    public FullCategoricalTensorIndexing(int numberOfCategories1, int numberOfCategories2,
                                         int numberOfIndices1, int numberOfIndices2) {
        this.numberOfCategories1 = numberOfCategories1;
        this.numberOfCategories2 = numberOfCategories2;
        this.numberOfIndices1 = numberOfIndices1;
        this.numberOfIndices2 = numberOfIndices2;
    }

    @Override
    public int getIndex(int a, int b, int i, int j) {
        return numberOfIndices2 * (numberOfIndices1 * (a * numberOfCategories2 + b) + i) + j;
    }

    @Override
    public int getEffectiveSize() {
        return numberOfCategories1 * numberOfCategories2 * numberOfIndices1 * numberOfIndices2;
    }

    @Override
    public boolean isStrictlySymmetric() {
        return false;
    }

    @Override
    public boolean isStrictlySymmetricByCategories() {
        return false;
    }

    @Override
    public int getNumberOfCategories1() {
        return numberOfCategories1;
    }

    @Override
    public int getNumberOfCategories2() {
        return numberOfCategories2;
    }

    @Override
    public int getNumberOfIndices1() {
        return numberOfIndices1;
    }

    @Override
    public int getNumberOfIndices2() {
        return numberOfIndices2;
    }
}
