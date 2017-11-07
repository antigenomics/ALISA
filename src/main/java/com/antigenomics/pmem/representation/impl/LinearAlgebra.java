package com.antigenomics.pmem.representation.impl;

public final class LinearAlgebra {
    static double dotProductSA(Vector a, Vector x) {
        double res = 0;

        for (VectorElement xi : x) {
            res += a.getAt(xi.getIndex()) * xi.getValue();
        }

        return res;
    }

    static double dotProductDD(Vector a, Vector x) {
        double res = 0;

        for (int i = 0; i < a.size; i++) {
            res += a.getAt(i) * x.getAt(i);
        }

        return res;
    }

    public static double dotProduct(Vector a, Vector x) {
        a.assertVectorCompatible(x);

        if (x.isSparse()) {
            if (a.isSparse() && x.getEffectiveSize() > a.getEffectiveSize()) {
                return dotProductSA(x, a);
            } else {
                return dotProductSA(a, x);
            }
        } else if (a.isSparse()) {
            return dotProductSA(x, a);
        } else {
            return dotProductDD(x, a);
        }
    }

    ///

    static double symmetricEnergyFormSparse(Matrix A, Vector x) {
        double res = 0;

        for (VectorElement xi : x) {
            final int i = xi.getIndex();
            final double xiValue = xi.getValue();
            for (VectorElement xj : x) {
                final int j = xj.getIndex();
                if (i < j) {
                    res += xiValue * xj.getValue() * A.getAt(i, j);
                }
            }
            res += xiValue * A.getAt(i, i);
        }

        return res;
    }

    static double symmetricEnergyFormDense(Matrix A, Vector x) {
        double res = 0;

        for (int i = 0; i < x.size; i++) {
            final double xi = x.getAt(i);
            for (int j = 0; j < i; j++) {
                res += xi * x.getAt(j) * A.getAt(i, j);
            }
            res += xi * A.getAt(i, i);
        }

        return res;
    }

    /**
     * Uses only upper triangular part of A. Value for diagonal is scaled with xi, not xi*xi
     *
     * @param A
     * @param x
     * @return
     */
    public static double symmetricEnergyForm(Matrix A, Vector x) {
        if (!A.isSquare()) {
            throw new IllegalArgumentException("Non-square matrix provided to symmetric form");
        }

        A.assertVectorCompatibleRow(x);

        if (x.isSparse()) {
            return symmetricEnergyFormSparse(A, x);
        } else {
            return symmetricEnergyFormDense(A, x);
        }
    }

    ///

    static double nonSymmetricEnergyFormSparse(Matrix A, Vector x, Vector y) {
        // xi should be the shortest
        double res = 0;

        for (VectorElement xi : x) {
            final int i = xi.getIndex();
            final double xiValue = xi.getValue();
            for (VectorElement yj : y) {
                res += xiValue * yj.getValue() * A.getAt(i, yj.getIndex());
            }
        }

        return res;
    }

    static double nonSymmetricEnergyFormSemiSparse(Matrix A, Vector x, Vector y) {
        double res = 0;

        for (VectorElement xi : x) {
            final int i = xi.getIndex();
            final double xiValue = xi.getValue();
            for (VectorElement xj : x) {
                final int j = xj.getIndex();
                if (i < j) {
                    res += xiValue * xj.getValue() * A.getAt(i, j);
                }
            }
            res += xiValue * A.getAt(i, i);
        }

        return res;
    }

    static double nonSymmetricEnergyFormDense(Matrix A, Vector x, Vector y) {
        // xi should be the shortest
        double res = 0;

        for (int i = 0; i < x.size; i++) {
            final double xi = x.getAt(i);
            for (int j = 0; j < y.size; j++) {
                res += xi * y.getAt(j) * A.getAt(i, j);
            }
        }

        return res;
    }

    /**
     * Uses only upper triangular part of A. Value for diagonal is scaled with xi, not xi*xi
     *
     * @param A
     * @param x
     * @return
     */
    public static double nonSymmetricEnergyForm(Matrix A, Vector x) {
        if (!A.isSquare()) {
            throw new IllegalArgumentException("Non-square matrix provided to symmetric form");
        }

        A.assertVectorCompatibleRow(x);

        if (x.isSparse()) {
            return symmetricEnergyFormSparse(A, x);
        } else {
            return symmetricEnergyFormDense(A, x);
        }
    }
}
