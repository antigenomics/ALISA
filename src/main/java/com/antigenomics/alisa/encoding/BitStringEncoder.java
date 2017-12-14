package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.entities.EntitySequence;
import com.antigenomics.alisa.entities.StateString;
import com.antigenomics.alisa.entities.StateStringSequence;

public abstract class BitStringEncoder<E extends Encoding> implements Encoder<StateString, E> {
    protected final int length;

    public BitStringEncoder(int length) {
        this.length = length;

        if (length <= 0) {
            throw new IllegalArgumentException("Length should be greater than 0");
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    public EntitySequence<StateString> getGenerator() {
        return new StateStringSequence(2, length);
    }
}
