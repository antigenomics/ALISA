package com.antigenomics.pmem.representation.algebra.matrix;

import com.antigenomics.pmem.representation.LinearSpaceObjectUtils;

public abstract class DenseMatrix
        extends SafeRealMatrix {
    protected final double[] elements;
    protected final MatrixStorageIndex indexing;

    public DenseMatrix(double[] elements, MatrixStorageIndex indexing) {
        this.elements = elements;
        this.indexing = indexing;
    }

    private double bfSS(RealVector a, RealVector b) {
        if (a.getEffectiveSize() > b.getEffectiveSize()) {
            // outer loop should be ran for the smallest of vectors
            return bfSS2(a, b);
        }

        double res = 0;
        for (RealVectorElement ai : a) {
            final double aValue = ai.getValue();
            final int i = ai.getIndex();
            for (RealVectorElement bj : b) {
                res += aValue * getAt(i, bj.getIndex()) * bj.getValue();
            }
        }
        return res;
    }

    private double bfSS2(RealVector a, RealVector b) {
        double res = 0;
        for (RealVectorElement bj : b) {
            final double bValue = bj.getValue();
            final int j = bj.getIndex();
            for (RealVectorElement ai : a) {
                res += ai.getValue() * getAt(ai.getIndex(), j) * bValue;
            }
        }
        return res;
    }

    private double bfSD(RealVector a, RealVector b) {
        double res = 0;
        for (RealVectorElement ai : a) {
            final double value = ai.getValue();
            final int i = ai.getIndex();
            for (int j = 0; j < b.getSize(); j++) {
                res += value * getAt(i, j) * b.getAt(j);
            }
        }
        return res;
    }

    private double bfDS(RealVector a, RealVector b) {
        double res = 0;
        for (RealVectorElement bj : b) {
            final double bValue = bj.getValue();
            final int j = bj.getIndex();
            for (int i = 0; i < a.getSize(); i++) {
                res += a.getAt(i) * getAt(i, j) * bValue;
            }
        }
        return res;
    }

    private double bfDD(RealVector a, RealVector b) {
        if (a.getEffectiveSize() > b.getEffectiveSize()) {
            // outer loop should be ran for the smallest of vectors
            return bfSS2(a, b);
        }

        double res = 0;
        for (int i = 0; i < a.getSize(); i++) {
            final double aValue = a.getAt(i);
            for (int j = 0; j < b.getSize(); j++) {
                res += aValue * getAt(i, j) * b.getAt(j);
            }
        }
        return res;
    }

    private double bfDD2(RealVector a, RealVector b) {
        double res = 0;
        for (int j = 0; j < b.getSize(); j++) {
            final double bValue = b.getAt(j);
            for (int i = 0; i < a.getSize(); i++) {
                res += a.getAt(i) * getAt(i, j) * bValue;
            }
        }
        return res;
    }

    @Override
    protected double bilinearFormUnchecked(RealVector a, RealVector b) {
        if (a.isSparse()) {
            if (b.isSparse()) {
                return bfSS(a, b);
            } else {
                return bfSD(a, b);
            }
        } else if (b.isSparse()) {
            return bfDS(a, b);
        } else {
            return bfDD(a, b);
        }
    }

    @Override
    protected final double bilinearFormUnchecked(RealVector a) {
        if (a.isSparse()) {
            return bfSS2(a, a);
        } else {
            return bfDD2(a, a);
        }
    }

    @Override
    public int getNumberOfRows() {
        return indexing.getNumberOfRows();
    }

    @Override
    public int getNumberOfColumns() {
        return indexing.getNumberOfColumns();
    }

    @Override
    public final double getAt(int i, int j) {
        return elements[indexing.getIndex(i, j)];
    }

    @Override
    public final double norm1() {
        return LinearSpaceObjectUtils.norm1(elements);
    }

    @Override
    public final double norm2() {
        return LinearSpaceObjectUtils.norm2(elements);
    }

    @Override
    public final boolean isSparse() {
        return false;
    }

    @Override
    public final int getEffectiveSize() {
        return getNumberOfRows() * getNumberOfColumns();
    }
}
