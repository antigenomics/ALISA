package com.antigenomics.pmem.representation;

public abstract class SafeImmutableLinearSpaceObject<O extends ImmutableLinearSpaceObject<O>>
        implements ImmutableLinearSpaceObject<O> {
    protected abstract O plusUnchecked(O other);

    protected abstract boolean isCompatible(O other);

    @Override
    public final O plus(O other) {
        if (isCompatible(other)) {
            return plusUnchecked(other);
        }
        throw new IllegalArgumentException("Linear object " + this + " is not compatible with " + other);
    }
}