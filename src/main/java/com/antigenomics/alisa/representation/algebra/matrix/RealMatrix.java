package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.algebra.BilinearMap;
import com.antigenomics.alisa.representation.algebra.ElementContainer;

public interface RealMatrix
        extends ElementContainer<RealMatrixElement>, BilinearMap<RealVector, RealMatrix>,
        MockRealMatrix {
}
