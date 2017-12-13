package com.antigenomics.alisa.impl;

import java.util.Arrays;

public class EncodingUtils {
    static int fromMilibAminoAcid(byte aa) {
        return aa - 1;
    }

    static byte toMilibAminoAcid(int aa) {
        return (byte) (aa + 1);
    }

    static final int AA_COUNT = 20;

    // The main recursive method to print all possible strings of length k
    public static void printAllKLengthRec(int[] arr, int n, int k) {
        if (k == 0) {
            System.out.println(Arrays.toString(arr));
            return;
        }

        for (int i = 0; i < n; ++i) {
            int[] newArr = Arrays.copyOf(arr, arr.length);
            newArr[k - 1] = i;
            printAllKLengthRec(newArr, n, k - 1);
        }
    }
}
