package com.antigenomics.alisa.algebra;

public interface TypedCloneable<T extends TypedCloneable<T>> {
    T deepCopy();
}
