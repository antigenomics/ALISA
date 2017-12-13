package com.antigenomics.alisa.entities;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface EntityGenerator<E extends Entity> extends Iterable<E> {
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(),
                getSizeEstimate(),
                Spliterator.CONCURRENT | Spliterator.NONNULL | Spliterator.ORDERED);
    }

    long getSizeEstimate();
}
