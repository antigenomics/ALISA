package com.antigenomics.pmem.impl;

import com.antigenomics.pmem.entities.BitString;
import com.antigenomics.pmem.estimator.mc.EntityMutator;
import com.sun.istack.internal.NotNull;

import java.util.Arrays;

import static com.antigenomics.pmem.estimator.mc.MonteCarloUtils.MC_RND;

public class BitStringMutator implements EntityMutator<BitString> {
    @Override
    public BitString mutate(@NotNull final BitString entity) {
        boolean[] bitSet = Arrays.copyOf(entity.getBits(), entity.getBits().length);

        final int mutationPos = MC_RND.nextInt(bitSet.length);

        bitSet[mutationPos] = !bitSet[mutationPos];

        return new BitString(bitSet);
    }
}
