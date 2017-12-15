package com.antigenomics.alisa.entities;

import com.antigenomics.alisa.state.State;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

// todo: need to make both full and random generators available
// todo: perhaps create a factory and unify interfaces, the stream API is a problem here - need to choose correct semantics
// todo: as we'll want to use both 'for/next' a '.stream' semantics (?)

public final class StateStringSequence implements EntitySequence<StateString> {
    private final int bufferSize, numberOfElements, arrayLength;
    private final long sizeEstimate;

    public StateStringSequence(int numberOfElements, int arrayLength) {
        this(numberOfElements, arrayLength, 4096);
    }

    public StateStringSequence(int numberOfElements, int arrayLength,
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
        return new StateArrayBuffer<>(Function.identity());
    }

    @Override
    public <S extends State> Iterator<S> iterator(Function<StateString, S> conversion) {
        return new StateArrayBuffer<>(conversion);
    }

    private class StateArrayBuffer<S> implements Iterator<S> {
        final LinkedBlockingQueue<StateString> queue = new LinkedBlockingQueue<>(bufferSize);
        final Function<StateString, S> conv;
        volatile StateString next;

        StateArrayBuffer(Function<StateString, S> conv) {
            this.conv = conv;
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
        public S next() {
            return conv.apply(next);
        }
    }
}