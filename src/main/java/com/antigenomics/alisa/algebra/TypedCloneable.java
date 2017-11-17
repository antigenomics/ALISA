package com.antigenomics.alisa.algebra;

import java.io.Serializable;

public interface TypedCloneable<T extends TypedCloneable<T>>
        extends Serializable {
    T deepCopy();
}
