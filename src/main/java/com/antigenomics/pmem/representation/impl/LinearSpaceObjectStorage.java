package com.antigenomics.pmem.representation.impl;

import com.antigenomics.pmem.encoding.EncodingUnit;
import com.antigenomics.pmem.representation.LinearSpaceObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface LinearSpaceObjectStorage<E extends LinearSpaceObjectElement, S extends LinearSpaceObjectStorage<E, S>>
        extends Iterable<E>, LinearSpaceObject<S>, EncodingUnit {
    int[] getSizes();

    default int[] getEffectiveSizes() {
        if (isSparse()) {
            throw new NotImplementedException();
        } else {
            return getSizes();
        }
    }

    double getAt(int[] indices);

    int getDimensions();

    boolean isSparse();

    default void assertCompatible(LinearSpaceObjectStorage other) {
        if (this.getDimensions() != other.getDimensions()) {
            throw new IllegalArgumentException(this.toString() +
                    "is incompatible to" +
                    other.toString() + ": number dimensions don't match");
        }
        for (int i = 0; i < getSizes().length; i++) {
            if (this.getSizes()[i] != other.getSizes()[i]) {
                throw new IllegalArgumentException(this.toString() +
                        "is incompatible to" +
                        other.toString() + ": sizes at dimension#" + i + " don't match");
            }
        }
    }
}
