package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.encoding.Encoding;
import com.antigenomics.alisa.entities.EntityGenerator;
import com.antigenomics.alisa.entities.StateString;
import com.antigenomics.alisa.entities.StateStringGenerator;

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
    public EntityGenerator<StateString> getGenerator() {
        return new StateStringGenerator(2, length);
    }
}
