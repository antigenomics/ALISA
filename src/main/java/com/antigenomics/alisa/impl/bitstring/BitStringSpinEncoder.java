package com.antigenomics.alisa.impl.bitstring;

import com.antigenomics.alisa.algebra.matrix.DenseVector;
import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.BitString;

import java.util.Arrays;

public class BitStringSpinEncoder implements Encoder<BitString, DenseVector> {
    private final int length;

    public BitStringSpinEncoder(int length) {
        this.length = length;

        if (length <= 0) {
            throw new IllegalArgumentException("Length should be greater than 0");
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    public DenseVector encode(BitString value) {
        if (value.getBits().length != length) {
            throw new IllegalArgumentException("Lengths don't match");
        }

        double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            values[i] = value.getAt(i) ? 0.5 : -0.5;
        }

        return new DenseVector(values, false);
    }

    @Override
    public DenseVector getZero() {
        double[] values = new double[length];
        Arrays.fill(values, -0.5);
        return new DenseVector(values, false);
    }
}
