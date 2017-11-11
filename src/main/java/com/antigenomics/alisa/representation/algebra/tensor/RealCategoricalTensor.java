package com.antigenomics.alisa.representation.algebra.tensor;

import com.antigenomics.alisa.representation.algebra.BilinearMap;
import com.antigenomics.alisa.representation.algebra.ElementContainer;

public interface RealCategoricalTensor
        extends ElementContainer<RealCategoricalTensorElement>, BilinearMap<CategoricalVector, RealCategoricalTensor>,
        RealCategoricalTensorAccessor, CategoricalTensorShape {
}
