package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.MutableLinearSpaceObject;
import com.antigenomics.pmem.representation.SafeMutableLinearSpaceObject;

public abstract class MutableRealMatrix
        extends SafeMutableLinearSpaceObject<RealMatrix>
        implements RealMatrixAccessors {
    @Override
    protected final void plusInplaceUnchecked(RealMatrix other) {
        plusInplaceUnchecked((RealMatrixAccessors) other);
    }

    @Override
    protected final void minusInplaceUnchecked(RealMatrix other) {
        minusInplaceUnchecked((RealMatrixAccessors) other);
    }

    @Override
    protected final void plusInplaceUnchecked(MutableLinearSpaceObject<RealMatrix> other) {
        plusInplaceUnchecked((RealMatrixAccessors) other);
    }

    @Override
    protected final void minusInplaceUnchecked(MutableLinearSpaceObject<RealMatrix> other) {
        minusInplaceUnchecked((RealMatrixAccessors) other);
    }

    protected abstract void plusInplaceUnchecked(RealMatrixAccessors other);

    protected abstract void minusInplaceUnchecked(RealMatrixAccessors other);

    @Override
    protected final boolean isCompatible(RealMatrix other) {
        return isStrictlySymmetric() == other.isStrictlySymmetric() &&
                getNumberOfRows() == other.getNumberOfRows() &&
                getNumberOfColumns() == other.getNumberOfColumns();
    }

    @Override
    protected final boolean isCompatible(MutableLinearSpaceObject<RealMatrix> other) {
        if (other instanceof RealMatrixAccessors) {
            RealMatrixAccessors otherConv = (RealMatrixAccessors) other;
            return otherConv.isStrictlySymmetric() == isStrictlySymmetric() &&
                    getNumberOfRows() == otherConv.getNumberOfRows() &&
                    getNumberOfColumns() == otherConv.getNumberOfColumns();
        }
        return false;
    }
}