package com.antigenomics.alisa.impl.bitstring;

import com.antigenomics.alisa.entities.BitString;
import com.antigenomics.alisa.estimator.mc.EntityMutator;
import com.antigenomics.alisa.estimator.mc.MonteCarloUtils;

import java.util.Arrays;

public class BitStringMutator implements EntityMutator<BitString> {
    @Override
    public BitString mutate(final BitString entity) {
        boolean[] bitSet = Arrays.copyOf(entity.getBits(), entity.getBits().length);

        final int mutationPos = MonteCarloUtils.nextInt(bitSet.length);

        bitSet[mutationPos] = !bitSet[mutationPos];

        return new BitString(bitSet);
    }
}
