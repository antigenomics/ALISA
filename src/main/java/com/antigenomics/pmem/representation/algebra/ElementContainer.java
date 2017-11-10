package com.antigenomics.pmem.representation.algebra;

public interface ElementContainer<E extends Element>
        extends Iterable<E> {
    boolean isSparse();

    int getEffectiveSize();
}
