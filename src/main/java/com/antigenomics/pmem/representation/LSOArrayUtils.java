package com.antigenomics.pmem.representation;

import com.antigenomics.pmem.representation.HasNorm;

import java.util.List;

public class LSOArrayUtils {
    public static double norm1(List<? extends HasNorm> objectList) {
        double res = 0;

        for (HasNorm obj : objectList)
            res += obj.norm1();

        return res;
    }

    public static double norm2(List<? extends HasNorm> objectList) {
        double res = 0;

        for (HasNorm obj : objectList) {
            final double norm2 = obj.norm2();
            res += norm2 * norm2;
        }

        return Math.sqrt(res);
    }
}
