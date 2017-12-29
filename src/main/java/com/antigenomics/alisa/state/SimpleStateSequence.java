package com.antigenomics.alisa.state;

import com.antigenomics.alisa.entities.Entity;
import com.antigenomics.alisa.entities.EntitySequence;

import java.util.Iterator;
import java.util.function.Function;

public class SimpleStateSequence<E extends Entity,
        S extends State> implements StateSequence<S> {
    private final EntitySequence<E> entitySequence;
    private final Function<E, S> conversion;

    public SimpleStateSequence(EntitySequence<E> entitySequence,
                               Function<E, S> conversion) {
        this.entitySequence = entitySequence;
        this.conversion = conversion;
    }

    @Override
    public Iterator<S> iterator() {
        return entitySequence.iterator(conversion);
    }

    @Override
    public long getCharacteristicSize() {
        return entitySequence.getCharacteristicSize();
    }

    @Override
    public StateSpace<S> asStateSpace() {
        return () -> this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleStateSequence<?, ?> that = (SimpleStateSequence<?, ?>) o;

        if (!entitySequence.equals(that.entitySequence)) return false;
        return conversion.equals(that.conversion);
    }

    @Override
    public int hashCode() {
        int result = entitySequence.hashCode();
        result = 31 * result + conversion.hashCode();
        return result;
    }
}
