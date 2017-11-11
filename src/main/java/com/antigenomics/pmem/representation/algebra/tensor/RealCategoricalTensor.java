package com.antigenomics.pmem.representation.algebra.tensor;

import com.antigenomics.pmem.representation.algebra.BilinearMap;
import com.antigenomics.pmem.representation.algebra.ElementContainer;

public interface RealCategoricalTensor
        extends ElementContainer<RealCategoricalTensorElement>, BilinearMap<CategoricalVector, RealCategoricalTensor>,
        RealCategoricalTensorAccessor, CategoricalTensorShape {
}
