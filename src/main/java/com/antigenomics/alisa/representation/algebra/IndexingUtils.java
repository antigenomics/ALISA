package com.antigenomics.alisa.representation.algebra;

public final class IndexingUtils {
    private IndexingUtils() {
    }

    public static int getFullMatrixIndex(int i, int j,
                                         /*int n,*/ int m) {
        return i * m + j;
    }

    public static int getTriangularMatrixIndex(int i, int j
                                         /*int n, int m*/) {
        if (j > i) {
            return getTriangularMatrixIndex(j, i);
        } else {
            return getTriangularMatrixLength(i) + j;
        }
    }

    public static int getFullTensorIndex(int i, int j, int a, int b,
                                         /*int n,*/ int m, int k, int l) {
        int i1 = getFullMatrixIndex(i, j, m),
                i2 = getFullMatrixIndex(a, b, l);

        return getFullMatrixIndex(i1, i2, k * l);
    }

    public static int getSemiTriangularTensorIndex(int i, int j, int a, int b,
                                                   /*int n,*/ int m, /*int k,*/ int l) {
        int i1 = getFullMatrixIndex(i, j, m),
                i2 = getTriangularMatrixIndex(a, b);

        return getFullMatrixIndex(i1, i2, getTriangularMatrixLength(l));
    }

    public static int getTriangularTensorIndex(int i, int j, int a, int b,
                                                   /*int n, int m, int k,*/ int l) {
        int i1 = getTriangularMatrixIndex(i, j),
                i2 = getTriangularMatrixIndex(a, b);

        return getFullMatrixIndex(i1, i2, getTriangularMatrixLength(l));
    }

    public static int getTriangularMatrixLength(int n) {
        return (n * (n + 1)) / 2;
    }

    public static int getTriangularMatrixSize(int length) {
        int n = (int) ((-1 + Math.sqrt(1 + 8 * length)) / 2);

        if (getTriangularMatrixLength(n) == length) {
            throw new IllegalArgumentException("Wrong number of elements for a " +
                    "triangular matrix.");
        }

        return n;
    }
}
