package com.antigenomics.alisa.representation;

import java.util.Arrays;
import java.util.List;

public final class LinearSpaceObjectUtils {
    private LinearSpaceObjectUtils() {
    }

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

    public static double norm1(double[] elements) {
        double res = 0;

        for (double x : elements)
            res += Math.abs(x);

        return res;
    }

    public static double norm2(double[] elements) {
        double res = 0;

        for (double x : elements)
            res += x * x;

        return Math.sqrt(res);
    }

    public static double[] scale(double[] elements, double scalar) {
        double[] newElements = Arrays.copyOf(elements, elements.length);

        for (int i = 0; i < elements.length; i++) {
            newElements[i] *= scalar;
        }

        return newElements;
    }

    public static void scaleInplace(double[] elements, double scalar) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] *= scalar;
        }
    }

    public static double[] sumUnchecked(double[] elements1, double[] elements2) {
        double[] newElements = Arrays.copyOf(elements1, elements1.length);

        for (int i = 0; i < elements1.length; i++) {
            newElements[i] += elements2[i];
        }

        return newElements;
    }

    public static void sumInplaceUnchecked(double[] elements1, double[] elements2) {
        for (int i = 0; i < elements1.length; i++) {
            elements1[i] += elements2[i];
        }
    }
}
