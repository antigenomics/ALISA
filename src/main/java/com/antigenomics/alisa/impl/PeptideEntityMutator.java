package com.antigenomics.alisa.impl;

import com.antigenomics.alisa.entities.Peptide;
import com.antigenomics.alisa.estimator.mc.EntityMutator;
import com.milaboratory.core.sequence.AminoAcidSequence;
import com.sun.istack.internal.NotNull;

import static com.antigenomics.alisa.estimator.mc.MonteCarloUtils.*;
import static com.antigenomics.alisa.impl.EncodingUtils.*;

public class PeptideEntityMutator implements EntityMutator<Peptide> {
    @Override
    public Peptide mutate(@NotNull final Peptide entity) {
        return new Peptide(mutateOnce(entity.getSequence()));
    }

    static AminoAcidSequence mutateOnce(@NotNull final AminoAcidSequence sequence) {
        final byte[] aaBytes = sequence.asArray();
        final int mutationPos = MC_RND.nextInt(aaBytes.length);
        byte mutatedBase;

        // Force replacement mutation, there is 1/20 chance that we'll change to the same AA
        // Its far better to waste time here than computing energy for the same AA sequence in MC
        while ((mutatedBase = toMilibAminoAcid(MC_RND.nextInt(AA_COUNT))) == aaBytes[mutationPos]) ;

        aaBytes[mutationPos] = mutatedBase;

        return new AminoAcidSequence(aaBytes);
    }
}
