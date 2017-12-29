package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.algebra.tensor.CategoricalVector;
import com.antigenomics.alisa.algebra.tensor.CategoryWeightPair;
import com.antigenomics.alisa.entities.StateString;

public final class BitStringCategoricalEncoder extends BitStringEncoder<CategoricalVector> {
    public BitStringCategoricalEncoder(int length) {
        super(length);
    }

    @Override
    public CategoricalVector encode(StateString value) {
        if (value.getLength() != length) {
            throw new IllegalArgumentException("Lengths don't match");
        }

        if (value.getNumberOfStates() != 2) {
            throw new IllegalArgumentException("Bit string should have exactly 2 states");
        }

        CategoryWeightPair[] values = new CategoryWeightPair[length];
        for (int i = 0; i < length; i++) {
            values[i] = new CategoryWeightPair(value.getAt(i));
        }

        return new CategoricalVector(values, 2, false);
    }

    @Override
    public CategoricalVector getZero() {
        CategoryWeightPair[] values = new CategoryWeightPair[length];
        for (int i = 0; i < length; i++) {
            values[i] = new CategoryWeightPair(0);
        }
        return new CategoricalVector(values, 2, false);
    }
}