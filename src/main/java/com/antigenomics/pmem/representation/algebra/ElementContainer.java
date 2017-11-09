package com.antigenomics.pmem.representation.algebra;

public interface ElementContainer<T>
        extends Iterable<T> {
    boolean isSparse();

    int getEffectiveSize();
}
