package com.antigenomics.alisa.representation.algebra;

public final class IndexingUtils {
    private IndexingUtils() {
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
