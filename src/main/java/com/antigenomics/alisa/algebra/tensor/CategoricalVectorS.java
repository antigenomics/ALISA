package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

public final class CategoricalVectorS
        extends CategoricalVector {
    public CategoricalVectorS(CategoryWeightPair[] elements, int numberOfCategories) {
        super(elements, numberOfCategories);
    }

    @Override
    public Tensor outerProduct(CategoricalVector b) {
        assert numberOfCategories == b.numberOfCategories;

        double[] tensorElements = new double[elements.length * b.elements.length *
                numberOfCategories * b.numberOfCategories];

        for (int i = 0; i < elements.length; i++) {
            CategoryWeightPair val1 = elements[i];
            for (int j = 0; j < b.elements.length; j++) {
                CategoryWeightPair val2 = b.elements[i];
                tensorElements[LinearAlgebraUtils.getSemiTriangularTensorIndex(i, j,
                        val1.getCategory(), val2.getCategory(),
                        b.elements.length, numberOfCategories)] = val1.getWeight() * val2.getWeight();
            }
        }

        return new SemiSymmetricTensor(tensorElements, elements.length,
                b.elements.length, numberOfCategories);
    }
}
