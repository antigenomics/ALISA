package com.antigenomics.alisa.representation.algebra.tensor;

import com.antigenomics.alisa.representation.algebra.IndexingUtils;

public class FullCategoricalTensorIndexing
        implements CategoricalTensorIndexing {
    private final int numberOfIndices1,
            numberOfIndices2,
            numberOfCategories1,
            numberOfCategories2;

    public FullCategoricalTensorIndexing(int numberOfIndices1, int numberOfIndices2,
                                         int numberOfCategories1, int numberOfCategories2) {
        this.numberOfIndices1 = numberOfIndices1;
        this.numberOfIndices2 = numberOfIndices2;
        this.numberOfCategories1 = numberOfCategories1;
        this.numberOfCategories2 = numberOfCategories2;
    }

    @Override
    public int getIndex(int i, int j, int a, int b) {
        return IndexingUtils.getFullTensorIndex(i, j, a, b,
                numberOfIndices2, numberOfCategories1, numberOfCategories2);
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
    public boolean isStrictlySemiSymmetric() {
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
