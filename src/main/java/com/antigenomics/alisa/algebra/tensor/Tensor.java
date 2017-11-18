package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.Container;
import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.algebra.VectorMapping;

public class Tensor
        implements LinearSpaceObject<Tensor>,
        VectorMapping<CategoricalVector, Tensor>, Container<IndexedTensorValue, Tensor> {
}
