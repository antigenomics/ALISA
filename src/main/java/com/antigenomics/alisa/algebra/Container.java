package com.antigenomics.alisa.algebra;

import java.io.Serializable;

public interface Container<V extends IndexedValue<V>, C extends Container<V, C>>
        extends Iterable<V>, TypedCloneable<C>, Serializable {
    boolean isSparse();

    double getAt(int... indices);

    int getEffectiveSize();

    C asSparse();

    C asDense();
}
