package com.antigenomics.pmem.representation;

import com.antigenomics.pmem.encoding.EncodingUnit;
import com.sun.istack.internal.NotNull;

public interface Matrix<M extends Matrix<M>> extends LinearSpaceObject<M>, EncodingUnit {
    M multiply(@NotNull final M other);

    M transpose();

    default M asSymmetric() {
        return (M) this;
    }
}
