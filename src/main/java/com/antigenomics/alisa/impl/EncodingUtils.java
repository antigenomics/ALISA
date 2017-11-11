package com.antigenomics.alisa.impl;

public class EncodingUtils {
    static int fromMilibAminoAcid(byte aa) {
        return aa - 1;
    }

    static byte toMilibAminoAcid(int aa) {
        return (byte) (aa + 1);
    }

    static final int AA_COUNT = 20;
}
