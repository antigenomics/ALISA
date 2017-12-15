package com.antigenomics.alisa.statistics;

import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.algebra.matrix.Matrix;
import com.antigenomics.alisa.encoding.BitStringOneHotEncoder;
import com.antigenomics.alisa.hamiltonian.SingleLayerSpinGlassHamiltonian;
import com.antigenomics.alisa.state.SimpleOneLayerStateStringSequence;
import com.antigenomics.alisa.state.StateSequence;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExactStateProbabilityDistributionTest {
    @Test
    public void testInit() {
        BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(4);
        SingleLayerSpinGlassHamiltonian hamiltonian = new SingleLayerSpinGlassHamiltonian(bitStringSpinEncoder);

        StateSequence ss = new SimpleOneLayerStateStringSequence(2, 4);
        //ss.forEach(System.out::println);

        Matrix Hij = Matrix.DENSE(new double[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}});

        LinearSpaceObjectArray params = new LinearSpaceObjectArray(Arrays.asList(Hij));

        ExactStateProbabilityDistribution probs = new ExactStateProbabilityDistribution(hamiltonian,
                params, ss.asStateSpace());

        List states = (List) ss.stream().collect(Collectors.toList());

        probs.computeProbabilities(states).forEach(
                x -> assertEquals(0.0625, ((StateProbability) x).getProbability())
        );

        //System.out.println(probs.computeProbabilities((List) ss.stream().collect(Collectors.toList())));
    }
}
