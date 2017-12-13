package com.antigenomics.alisa.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StateArrayGenerator implements Iterable<int[]> {
    private final int bufferSize, numberOfElements, arrayLength;
    private final long sizeEstimate;

    public StateArrayGenerator(int numberOfElements, int arrayLength) {
        this(numberOfElements, arrayLength, 4096);
    }

    public StateArrayGenerator(int numberOfElements, int arrayLength,
                               int bufferSize) {
        this.numberOfElements = numberOfElements;
        this.arrayLength = arrayLength;
        this.bufferSize = bufferSize;
        double sizeEstimate = Math.pow(numberOfElements, arrayLength);

        this.sizeEstimate = sizeEstimate > Long.MAX_VALUE ? Long.MAX_VALUE : (long) sizeEstimate;
    }

    public long getSizeEstimate() {
        return sizeEstimate;
    }

    public Stream<int[]> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Stream<int[]> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public Iterator<int[]> iterator() {
        return new StateArrayIter();
    }

    @Override
    public Spliterator<int[]> spliterator() {
        return Spliterators.spliterator(iterator(), sizeEstimate,
                Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.IMMUTABLE |
                        Spliterator.CONCURRENT);
    }

    private class StateArrayIter implements Iterator<int[]> {
        private final AtomicInteger counter = new AtomicInteger(0);
        private final ConcurrentLinkedQueue<int[]> results;
        private final AtomicBoolean generatorFinished = new AtomicBoolean(false);

        StateArrayIter() {
            this.results = new ConcurrentLinkedQueue<>();

            new Thread(() -> {
                generateStatesRecursive(new int[arrayLength], numberOfElements, arrayLength);
                generatorFinished.set(true);
            }).start();
        }

        private void generateStatesRecursive(int[] arr, int n, int k) {
            while (counter.get() >= bufferSize) ;

            if (k == 0) {
                results.offer(arr);
                counter.incrementAndGet();
                return;
            }

            for (int i = 0; i < n; ++i) {
                int[] newArr = Arrays.copyOf(arr, arr.length);
                newArr[k - 1] = i;
                generateStatesRecursive(newArr, n, k - 1);
            }
        }

        @Override
        public boolean hasNext() {
            return !(results.isEmpty() && generatorFinished.get());
        }

        @Override
        public int[] next() {
            int[] res = null;

            while (res == null) {
                // wait as the element gets into the queue
                res = results.poll();
            }

            counter.decrementAndGet();

            return res;
        }
    }
}
