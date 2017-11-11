package com.antigenomics.alisa.representation.algebra;

public interface ElementContainer<E extends Element>
        extends Iterable<E> {
    boolean isSparse();

    int getEffectiveSize();
}
