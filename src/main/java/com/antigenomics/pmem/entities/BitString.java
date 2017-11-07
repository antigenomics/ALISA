package com.antigenomics.pmem.entities;

import com.sun.istack.internal.NotNull;

public class BitString implements Entity {
    private final boolean[] bits;

    public BitString(@NotNull final boolean[] bits) {
        this.bits = bits;
    }

    public boolean[] getBits() {
        return bits;
    }
}
