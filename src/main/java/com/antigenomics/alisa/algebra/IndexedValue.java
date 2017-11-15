package com.antigenomics.alisa.algebra;

public interface IndexedValue<V extends IndexedValue<V>>
        extends Comparable<V> {
    double getDoubleValue();

    int getIntValue();

    V add(V other);

    V scale(double scalar);
}
