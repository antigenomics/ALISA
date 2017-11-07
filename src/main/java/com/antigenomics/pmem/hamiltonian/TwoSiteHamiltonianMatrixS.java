package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.encoding.Encoder;
import com.antigenomics.pmem.entities.Entity;
import com.sun.istack.internal.NotNull;

public final class TwoSiteHamiltonianMatrixS<T extends Entity, M extends Matrix<M>>
        extends TwoSiteHamiltonianMatrixNS<T, T, M> {

    public TwoSiteHamiltonianMatrixS(@NotNull final Encoder<T, M> encoder) {
        super(encoder, encoder);
    }
}
