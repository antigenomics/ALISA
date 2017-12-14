package com.antigenomics.alisa.entities;

import com.antigenomics.alisa.Misc;

public final class AminoAcidString extends StateString {
    public AminoAcidString(int[] states) {
        super(states, 20);
    }

    public char getAA(int pos) {
        return Misc.AMINO_ACIDS[states[pos]];
    }
}
