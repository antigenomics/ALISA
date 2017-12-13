package com.antigenomics.alisa.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
        return new StateArrayBuffer();
    }

    @Override
    public Spliterator<int[]> spliterator() {
        return Spliterators.spliterator(iterator(),
                sizeEstimate,
                Spliterator.CONCURRENT | Spliterator.NONNULL | Spliterator.ORDERED);
    }

    private class StateArrayBuffer implements Iterator<int[]> {
        final LinkedBlockingQueue<int[]> queue = new LinkedBlockingQueue<>(bufferSize);
        final int[] LAST = new int[0];
        volatile int[] next;

        StateArrayBuffer() {
            new Thread(() -> {
                generateStatesRecursive(new int[arrayLength], numberOfElements, arrayLength);
                try {
                    queue.put(LAST);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        private void generateStatesRecursive(int[] arr, int n, int k) {
            if (k == 0) {
                try {
                    queue.put(arr);
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

        @Override
        public boolean hasNext() {
            try {
                next = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return next != LAST;
        }

        @Override
        public int[] next() {
            return next;
        }
    }
}