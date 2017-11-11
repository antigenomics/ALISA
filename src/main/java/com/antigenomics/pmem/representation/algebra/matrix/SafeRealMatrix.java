package com.antigenomics.pmem.representation.algebra.matrix;

public abstract class SafeRealMatrix
        implements RealMatrix {
    protected abstract double bilinearFormUnchecked(RealVector a, RealVector b);

    protected abstract double bilinearFormUnchecked(RealVector a);

    protected abstract RealMatrix plusUnchecked(RealMatrix a);

    protected abstract MatrixIndexing getIndexing();

    @Override
    public final double bilinearForm(RealVector a, RealVector b) {
        if (isCompatibleLeft(a) && isCompatibleRight(b))
            return bilinearFormUnchecked(a, b);
        else
            throw new IllegalArgumentException("Matrix " + this +
                    " is not compatible with left vector " + a + " and/or right vector " + b);
    }

    @Override
    public final double bilinearForm(RealVector a) {
        if (!isSquare())
            throw new IllegalArgumentException("Matrix " + this +
                    " is not square.");

        if (isCompatibleLeft(a))
            return bilinearFormUnchecked(a);
        else
            throw new IllegalArgumentException("Matrix " + this +
                    " is not compatible with vector " + a);
    }

    @Override
    public final RealMatrix plus(RealMatrix other) {
        if (isCompatible(other))
            return plusUnchecked(other);
        else
            throw new IllegalArgumentException("Matrix " + this +
                    " is not compatible with matrix " + other);
    }

    private boolean isCompatibleLeft(RealVector other) {
        return this.getNumberOfRows() == other.getSize();
    }

    private boolean isCompatibleRight(RealVector other) {
        return this.getNumberOfRows() == other.getSize();
    }

    private boolean isCompatible(RealMatrix other) {
        return this.getNumberOfRows() == other.getNumberOfRows() &&
                this.getNumberOfColumns() == other.getNumberOfColumns();
    }

    @Override
    public int getNumberOfRows() {
        return getIndexing().getNumberOfRows();
    }

    @Override
    public int getNumberOfColumns() {
        return getIndexing().getNumberOfColumns();
    }

    @Override
    public boolean isSquare() {
        return getIndexing().isSquare();
    }

    @Override
    public boolean isStrictlySymmetric() {
        return getIndexing().isStrictlySymmetric();
    }
}
