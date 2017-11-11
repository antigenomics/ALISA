package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.MutableLinearSpaceObject;
import com.antigenomics.alisa.representation.SafeMutableLinearSpaceObject;

public abstract class MutableRealMatrix
        extends SafeMutableLinearSpaceObject<RealMatrix>
        implements MockRealMatrix {
    @Override
    protected final void plusInplaceUnchecked(RealMatrix other) {
        plusInplaceUnchecked((RealMatrixAccessor) other);
    }

    @Override
    protected final void minusInplaceUnchecked(RealMatrix other) {
        minusInplaceUnchecked((RealMatrixAccessor) other);
    }

    @Override
    protected final void plusInplaceUnchecked(MutableLinearSpaceObject<RealMatrix> other) {
        plusInplaceUnchecked((RealMatrixAccessor) other);
    }

    @Override
    protected final void minusInplaceUnchecked(MutableLinearSpaceObject<RealMatrix> other) {
        minusInplaceUnchecked((RealMatrixAccessor) other);
    }

    protected abstract void plusInplaceUnchecked(RealMatrixAccessor other);

    protected abstract void minusInplaceUnchecked(RealMatrixAccessor other);

    protected abstract MatrixIndexing getIndexing();

    @Override
    public int getNumberOfRows() {
        return getIndexing().getNumberOfRows();
    }

    @Override
    public int getNumberOfColumns() {
        return getIndexing().getNumberOfColumns();
    }

    @Override
    public boolean isStrictlySymmetric() {
        return getIndexing().isStrictlySymmetric();
    }

    @Override
    protected final boolean isCompatible(RealMatrix other) {
        return isStrictlySymmetric() == other.isStrictlySymmetric() &&
                getNumberOfRows() == other.getNumberOfRows() &&
                getNumberOfColumns() == other.getNumberOfColumns();
    }

    @Override
    protected final boolean isCompatible(MutableLinearSpaceObject<RealMatrix> other) {
        if (other instanceof MockRealMatrix) {
            MockRealMatrix otherConv = (MockRealMatrix) other;

            return isStrictlySymmetric() == otherConv.isStrictlySymmetric() &&
                    getNumberOfRows() == otherConv.getNumberOfRows() &&
                    getNumberOfColumns() == otherConv.getNumberOfColumns();
        }
        return false;
    }
}