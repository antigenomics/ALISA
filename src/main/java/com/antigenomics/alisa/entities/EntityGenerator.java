package com.antigenomics.alisa.entities;

import com.antigenomics.alisa.Generator;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface EntityGenerator<E extends Entity> extends Generator<E> {
}
