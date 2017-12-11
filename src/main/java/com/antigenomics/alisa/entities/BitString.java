package com.antigenomics.alisa.entities;

public class BitString implements Entity {
    private final boolean[] bits;

    public BitString(final boolean[] bits) {
        this.bits = bits;
    }

    public boolean[] getBits() {
        return bits;
    }

    public boolean getAt(int index) {
        return bits[index];
    }
}
