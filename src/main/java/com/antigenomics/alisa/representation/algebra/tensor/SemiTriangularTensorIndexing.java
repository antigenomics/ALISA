package com.antigenomics.alisa.representation.algebra.tensor;

import com.antigenomics.alisa.representation.algebra.IndexingUtils;

public class SemiTriangularTensorIndexing
        implements CategoricalTensorIndexing {
    private final int numberOfIndices1,
            numberOfIndices2,
            numberOfCategories;

    public SemiTriangularTensorIndexing(int numberOfIndices1,
                                        int numberOfIndices2,
                                        int numberOfCategories) {
        this.numberOfIndices1 = numberOfIndices1;
        this.numberOfIndices2 = numberOfIndices2;
        this.numberOfCategories = numberOfCategories;
    }

    @Override
    public int getIndex(int i, int j, int a, int b) {
        return IndexingUtils.getSemiTriangularTensorIndex(i, j,
                a, b,
                numberOfIndices2, numberOfCategories);
    }

    public int getEffectiveSize() {
        return numberOfIndices1 * numberOfIndices2 *
                IndexingUtils.getTriangularMatrixLength(numberOfCategories);
    }

    @Override
    public boolean isStrictlySymmetric() {
        return false;
    }

    @Override
    public boolean isStrictlySemiSymmetric() {
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
        return numberOfIndices1;
    }

    @Override
    public int getNumberOfIndices2() {
        return numberOfIndices2;
    }
}