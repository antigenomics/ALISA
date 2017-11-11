package com.antigenomics.alisa.representation.algebra.tensor;

import com.antigenomics.alisa.representation.algebra.IndexingUtils;

public class TriangularCategoricalTensorIndexing
        implements CategoricalTensorIndexing {
    private final int numberOfIndices,
            numberOfCategories;

    public TriangularCategoricalTensorIndexing(int numberOfIndices,
                                               int numberOfCategories) {
        this.numberOfIndices = numberOfIndices;
        this.numberOfCategories = numberOfCategories;
    }

    @Override
    public int getIndex(int i, int j, int a, int b) {
        return IndexingUtils.getTriangularTensorIndex(i, j,
                a, b,
                numberOfCategories);
    }

    @Override
    public int getEffectiveSize() {
        return IndexingUtils.getTriangularMatrixLength(numberOfIndices) *
                IndexingUtils.getTriangularMatrixLength(numberOfCategories);
    }

    @Override
    public boolean isStrictlySymmetric() {
        return true;
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
        return numberOfIndices;
    }

    @Override
    public int getNumberOfIndices2() {
        return numberOfIndices;
    }
}
