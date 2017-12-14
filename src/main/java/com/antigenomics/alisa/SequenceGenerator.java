package com.antigenomics.alisa;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface SequenceGenerator<T> extends Iterable<T> {
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(),
                getCharacteristicSize(),
                Spliterator.CONCURRENT | Spliterator.NONNULL | Spliterator.ORDERED);
    }

    long getCharacteristicSize();
}
