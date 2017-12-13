package com.antigenomics.alisa.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
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
        return new StateArraySpliter();
    }

    private class StateArrayGen {
        protected final ArrayBlockingQueue<int[]> results;
        protected final AtomicBoolean generatorFinished = new AtomicBoolean(false);

        StateArrayGen() {
            this.results = new ArrayBlockingQueue<>(bufferSize);

            Thread thr = new Thread(() -> {
                generateStatesRecursive(new int[arrayLength], numberOfElements, arrayLength);
                generatorFinished.set(true);
            });

            thr.setDaemon(true);

            thr.start();
        }

        private void generateStatesRecursive(int[] arr, int n, int k) {
            if (k == 0) {
                try {
                    results.put(arr);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            for (int i = 0; i < n; ++i) {
                int[] newArr = Arrays.copyOf(arr, arr.length);
                newArr[k - 1] = i;
                generateStatesRecursive(newArr, n, k - 1);
            }
        }
    }

    private class StateArrayIter extends StateArrayGen implements Iterator<int[]> {
        @Override
        public boolean hasNext() {
            return !(results.isEmpty() && generatorFinished.get());
        }

        @Override
        public int[] next() {
            try {
                return results.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class StateArraySpliter extends StateArrayGen implements Spliterator<int[]> {
        @Override
        public synchronized boolean tryAdvance(Consumer<? super int[]> action) {
            if (results.isEmpty() && generatorFinished.get()) {
                return false;
            }

            try {
                action.accept(results.take());
                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Spliterator<int[]> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return sizeEstimate;
        }

        @Override
        public int characteristics() {
            return Spliterator.CONCURRENT | Spliterator.NONNULL | Spliterator.ORDERED;
        }
    }
}