package com.antigenomics.alisa;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface RandomSampler<T> {
    T sample();

    default List<T> sample(int count) {
        return IntStream.range(0, count).parallel()
                .mapToObj(x -> sample())
                .collect(Collectors.toList());
    }
}
