package com.antigenomics.alisa;

import java.util.List;

public interface Sampler<T> {
    T sample();

    List<T> sample(int count);
}
