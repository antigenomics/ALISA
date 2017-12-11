package com.antigenomics.alisa.impl.bitstring;

import com.antigenomics.alisa.algebra.matrix.IndexedVectorValue;
import com.antigenomics.alisa.algebra.matrix.SparseVector;
import com.antigenomics.alisa.algebra.matrix.Vector;
import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.BitString;

import java.util.LinkedList;
import java.util.List;

public class BitStringOneHotEncoder implements Encoder<BitString, SparseVector> {
    private final int length;

    public BitStringOneHotEncoder(int length) {
        this.length = length;

        if (length <= 0) {
            throw new IllegalArgumentException("Length should be greater than 0");
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    public SparseVector encode(BitString value) {
        if (value.getBits().length != length) {
            throw new IllegalArgumentException("Lengths don't match");
        }

        List<IndexedVectorValue> values = new LinkedList<>();

        for (int i = 0; i < length; i++) {
            if (value.getAt(i)) {
                values.add(new IndexedVectorValue(i));
            }
        }

        return new SparseVector(values, length, false);
    }

    @Override
    public SparseVector getZero() {
        return Vector.SPARSE_ZEROS(length);
    }
}