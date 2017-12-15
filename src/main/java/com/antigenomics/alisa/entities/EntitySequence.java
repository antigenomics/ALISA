package com.antigenomics.alisa.entities;

import com.antigenomics.alisa.SequenceGenerator;
import com.antigenomics.alisa.state.State;

import java.util.Iterator;
import java.util.function.Function;

public interface EntitySequence<E extends Entity> extends SequenceGenerator<E> {
    <S extends State> Iterator<S> iterator(Function<E, S> conversion);
}
