package com.antigenomics.alisa.impl.bitstring;

import com.antigenomics.alisa.algebra.tensor.CategoricalVector;
import com.antigenomics.alisa.algebra.tensor.CategoryWeightPair;
import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.BitString;

public class BitStringCategoricalEncoder implements Encoder<BitString, CategoricalVector> {
    private final int length;

    public BitStringCategoricalEncoder(int length) {
        this.length = length;

        if (length <= 0) {
            throw new IllegalArgumentException("Length should be greater than 0");
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    public CategoricalVector encode(BitString value) {
        if (value.getBits().length != length) {
            throw new IllegalArgumentException("Lengths don't match");
        }

        CategoryWeightPair[] values = new CategoryWeightPair[length];
        for (int i = 0; i < length; i++) {
            values[i] = new CategoryWeightPair(value.getAt(i) ? 0 : 1);
        }

        return new CategoricalVector(values, 2);
    }

    @Override
    public CategoricalVector getZero() {
        CategoryWeightPair[] values = new CategoryWeightPair[length];
        for (int i = 0; i < length; i++) {
            values[i] = new CategoryWeightPair(0);
        }
        return new CategoricalVector(values, 2);
    }
}