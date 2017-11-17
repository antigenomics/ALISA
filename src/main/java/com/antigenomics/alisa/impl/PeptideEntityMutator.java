package com.antigenomics.alisa.impl;

import com.antigenomics.alisa.entities.Peptide;
import com.antigenomics.alisa.estimator.mc.EntityMutator;
import com.antigenomics.alisa.estimator.mc.MonteCarloUtils;
import com.milaboratory.core.sequence.AminoAcidSequence;

import static com.antigenomics.alisa.impl.EncodingUtils.*;

public class PeptideEntityMutator implements EntityMutator<Peptide> {
    @Override
    public Peptide mutate(final Peptide entity) {
        return new Peptide(mutateOnce(entity.getSequence()));
    }

    static AminoAcidSequence mutateOnce(final AminoAcidSequence sequence) {
        final byte[] aaBytes = sequence.asArray();
        final int mutationPos = MonteCarloUtils.nextInt(aaBytes.length);
        byte mutatedBase;

        // Force replacement mutation, there is 1/20 chance that we'll change to the same AA
        // Its far better to waste time here than computing energy for the same AA sequence in MC
        while ((mutatedBase = toMilibAminoAcid(MonteCarloUtils.nextInt(AA_COUNT))) == aaBytes[mutationPos]) ;

        aaBytes[mutationPos] = mutatedBase;

        return new AminoAcidSequence(aaBytes);
    }
}
