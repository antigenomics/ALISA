package com.antigenomics.alisa.representation.algebra.tensor;

import com.antigenomics.alisa.representation.algebra.ElementContainer;
import com.antigenomics.alisa.representation.algebra.VectorSpace;

public interface CategoricalVector
        extends ElementContainer<CategoricalVectorElement>, VectorSpace<CategoricalVector, RealCategoricalTensor> {
    int getSize();

    int getNumberOfCategories();

    int getCategory(int index);
}
