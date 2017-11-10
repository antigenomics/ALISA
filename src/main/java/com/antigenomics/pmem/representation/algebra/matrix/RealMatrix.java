package com.antigenomics.pmem.representation.algebra.matrix;

import com.antigenomics.pmem.representation.algebra.BilinearMap;
import com.antigenomics.pmem.representation.algebra.ElementContainer;

public interface RealMatrix
        extends ElementContainer<RealMatrixElement>, BilinearMap<RealVector, RealMatrix>, RealMatrixAccessors {
}
