package com.antigenomics.alisa.algebra.tensor;

public class CategoricalVectorS
        extends CategoricalVector {
    public CategoricalVectorS(CategoryWeightPair[] elements, int numberOfCategories) {
        super(elements, numberOfCategories);
    }

    @Override
    public Tensor outerProduct(CategoricalVector b){
        // todo: make a semi-symmetric tensor
    }
}
