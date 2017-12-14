package com.antigenomics.alisa.entities;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public final class StateStringGenerator implements EntityGenerator<StateString> {
    private final int bufferSize, numberOfElements, arrayLength;
    private final long sizeEstimate;

    public StateStringGenerator(int numberOfElements, int arrayLength) {
        this(numberOfElements, arrayLength, 4096);
    }

    public StateStringGenerator(int numberOfElements, int arrayLength,
                                int bufferSize) {
        this.numberOfElements = numberOfElements;
        this.arrayLength = arrayLength;
        this.bufferSize = bufferSize;
        double sizeEstimate = Math.pow(numberOfElements, arrayLength);

        this.sizeEstimate = sizeEstimate > Long.MAX_VALUE ? Long.MAX_VALUE : (long) sizeEstimate;
    }

    @Override
    public long getCharacteristicSize() {
        return sizeEstimate;
    }

    @Override
    public Iterator<StateString> iterator() {
        return new StateArrayBuffer();
    }

    private class StateArrayBuffer implements Iterator<StateString> {
        final LinkedBlockingQueue<StateString> queue = new LinkedBlockingQueue<>(bufferSize);
        volatile StateString next;

        StateArrayBuffer() {
            new Thread(() -> {
                generateStatesRecursive(new int[arrayLength], numberOfElements, arrayLength);
                try {
                    queue.put(StateString.DUMMY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        private void generateStatesRecursive(int[] arr, int n, int k) {
            if (k == 0) {
                try {
                    queue.put(new StateString(arr, numberOfElements));
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

            return next != StateString.DUMMY;
        }

        @Override
        public StateString next() {
            return next;
        }
    }
}