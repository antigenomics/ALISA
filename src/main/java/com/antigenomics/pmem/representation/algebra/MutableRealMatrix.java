package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.MutableLinearSpaceObject;
import com.antigenomics.pmem.representation.SafeMutableLinearSpaceObject;

public abstract class MutableRealMatrix
        extends SafeMutableLinearSpaceObject<RealMatrix>
        implements RealMatrixAccessors {
    @Override
    protected void plusInplaceUnchecked(RealMatrix other) {
        plusInplaceUnchecked((RealMatrixAccessors) other);
    }

    @Override
    protected void minusInplaceUnchecked(RealMatrix other) {
        minusInplaceUnchecked((RealMatrixAccessors) other);
    }

    @Override
    protected void plusInplaceUnchecked(MutableLinearSpaceObject<RealMatrix> other) {
        plusInplaceUnchecked((RealMatrixAccessors) other);
    }

    @Override
    protected void minusInplaceUnchecked(MutableLinearSpaceObject<RealMatrix> other) {
        minusInplaceUnchecked((RealMatrixAccessors) other);
    }

    protected abstract void plusInplaceUnchecked(RealMatrixAccessors other);

    protected abstract void minusInplaceUnchecked(RealMatrixAccessors other);

    @Override
    protected final boolean isCompatible(RealMatrix other) {
        return isStrictlySymmetric() == other.isStrictlySymmetric() &&
                getSize1() == other.getSize1() &&
                getSize2() == other.getSize2();
    }

    @Override
    protected final boolean isCompatible(MutableLinearSpaceObject<RealMatrix> other) {
        if (other instanceof RealMatrixAccessors) {
            RealMatrixAccessors otherConv = (RealMatrixAccessors) other;
            return otherConv.isStrictlySymmetric() == isStrictlySymmetric() &&
                    getSize1() == otherConv.getSize1() &&
                    getSize2() == otherConv.getSize2();
        }
        return false;
    }
}