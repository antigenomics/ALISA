package com.antigenomics.pmem.representation.algebra.tensor;

import com.antigenomics.pmem.representation.algebra.ElementContainer;
import com.antigenomics.pmem.representation.algebra.VectorSpace;

public interface CategoricalVector
        extends ElementContainer<CategoricalVectorElement>, VectorSpace<CategoricalVector, RealCategoricalTensor> {
    int getSize();

    int getNumberOfCategories();

    int getCategory(int index);
}
