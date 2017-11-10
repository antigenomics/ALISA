package com.antigenomics.pmem.representation.algebra;

public interface Element<E extends Element<E, V>, V>
        extends Comparable<E> { // comparable in terms of order
    V getBoxedValue();
}