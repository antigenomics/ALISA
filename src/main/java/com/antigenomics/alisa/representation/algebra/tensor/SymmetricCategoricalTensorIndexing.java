package com.antigenomics.alisa.representation.algebra.tensor;

public class SymmetricCategoricalTensorIndexing
        implements CategoricalTensorIndexing {
    private final int numberOfCategories,
            numberOfIndices;

    public SymmetricCategoricalTensorIndexing(int numberOfCategories, int numberOfIndices) {
        this.numberOfCategories = numberOfCategories;
        this.numberOfIndices = numberOfIndices;
    }

    @Override
    public int getIndex(int a, int b, int i, int j) {
        return 0;
    }

    @Override
    public int getEffectiveSize() {
        return numberOfCategories * (numberOfCategories + 1) * numberOfIndices * (numberOfIndices + 1) / 4;
    }

    @Override
    public boolean isStrictlySymmetric() {
        return true;
    }

    @Override
    public boolean isStrictlySymmetricByCategories() {
        return true;
    }

    @Override
    public int getNumberOfCategories1() {
        return numberOfCategories;
    }

    @Override
    public int getNumberOfCategories2() {
        return numberOfCategories;
    }

    @Override
    public int getNumberOfIndices1() {
        return numberOfIndices;
    }

    @Override
    public int getNumberOfIndices2() {
        return numberOfIndices;
    }
}
