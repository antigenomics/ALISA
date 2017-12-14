package com.antigenomics.alisa.entities;

public final class BitString extends StateString {
    public BitString(int[] states) {
        super(states, 2);
    }

    public boolean getBit(int pos) {
        return states[pos] > 0;
    }
}
