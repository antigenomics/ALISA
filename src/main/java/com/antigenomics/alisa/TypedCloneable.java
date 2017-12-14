package com.antigenomics.alisa;

import java.io.Serializable;

public interface TypedCloneable<T extends TypedCloneable<T>>
        extends Serializable {
    T deepCopy();
}
