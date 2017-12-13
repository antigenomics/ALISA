package com.antigenomics.alisa.impl.bitstring;

import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.encoding.Encoding;
import com.antigenomics.alisa.entities.BitString;

import java.util.List;

public abstract class BitStringEncoder<E extends Encoding> implements Encoder<BitString, E> {
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
    public List<BitString> listPossibleStates() {
        return null;
    }
}
