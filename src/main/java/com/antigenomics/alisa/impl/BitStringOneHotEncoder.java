package com.antigenomics.alisa.impl;

import com.antigenomics.alisa.algebra.matrix.IndexedVectorValue;
import com.antigenomics.alisa.algebra.matrix.SparseVector;
import com.antigenomics.alisa.algebra.matrix.Vector;
import com.antigenomics.alisa.entities.StateString;

import java.util.LinkedList;
import java.util.List;

public class BitStringOneHotEncoder extends BitStringEncoder<SparseVector> {
    public BitStringOneHotEncoder(int length) {
        super(length);
    }

    @Override
    public SparseVector encode(StateString value) {
        if (value.getLength() != length) {
            throw new IllegalArgumentException("Lengths don't match");
        }

        if (value.getNumberOfStates() != 2) {
            throw new IllegalArgumentException("Bit string should have exactly 2 states");
        }

        List<IndexedVectorValue> values = new LinkedList<>();

        for (int i = 0; i < length; i++) {
            if (value.getAt(i) > 0) {
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