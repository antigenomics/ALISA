package com.antigenomics.alisa.representation;

public abstract class SafeMutableLinearSpaceObject<O extends ImmutableLinearSpaceObject<O>>
        implements MutableLinearSpaceObject<O> {
    protected abstract boolean isCompatible(O other);

    protected abstract boolean isCompatible(MutableLinearSpaceObject<O> other);

    protected abstract void plusInplaceUnchecked(O other);

    protected abstract void minusInplaceUnchecked(O other);

    protected abstract void plusInplaceUnchecked(MutableLinearSpaceObject<O> other);

    protected abstract void minusInplaceUnchecked(MutableLinearSpaceObject<O> other);

    @Override
    public final void plusInplace(O other) {
        if (isCompatible(other)) {
            plusInplaceUnchecked(other);
        } else {
            throw new IllegalArgumentException("Linear object " + this + " is not compatible with " + other);
        }
    }

    @Override
    public final void minusInplace(O other) {
        if (isCompatible(other)) {
            minusInplaceUnchecked(other);
        } else {
            throw new IllegalArgumentException("Linear object " + this + " is not compatible with " + other);
        }
    }

    @Override
    public final void plusInplace(MutableLinearSpaceObject<O> other) {
        if (isCompatible(other)) {
            plusInplaceUnchecked(other);
        } else {
            throw new IllegalArgumentException("Linear object " + this + " is not compatible with " + other);
        }
    }

    @Override
    public final void minusInplace(MutableLinearSpaceObject<O> other) {
        if (isCompatible(other)) {
            minusInplaceUnchecked(other);
        } else {
            throw new IllegalArgumentException("Linear object " + this + " is not compatible with " + other);
        }
    }
}
