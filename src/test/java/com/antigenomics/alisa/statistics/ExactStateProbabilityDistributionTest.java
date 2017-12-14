package com.antigenomics.alisa.statistics;

import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.algebra.matrix.Matrix;
import com.antigenomics.alisa.encoding.BitStringOneHotEncoder;
import com.antigenomics.alisa.entities.BitString;
import com.antigenomics.alisa.hamiltonian.SingleLayerSpinGlassHamiltonian;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ExactStateProbabilityDistributionTest {
    @Test
    public void testInit() {
       /* BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(2);
        SingleLayerSpinGlassHamiltonian hamiltonian = new SingleLayerSpinGlassHamiltonian(bitStringSpinEncoder);

        BitString s11 = new BitString(new int[]{1, 1}),
                s01 = new BitString(new int[]{0, 1}),
                s10 = new BitString(new int[]{1, 0}),
                s00 = new BitString(new int[]{0, 0});

        Matrix Hij = Matrix.DENSE(new double[][]{{0, 0}, {0, 0}});

        LinearSpaceObjectArray params = new LinearSpaceObjectArray(Arrays.asList(Hij));

        ExactStateProbabilityDistribution probs = new ExactStateProbabilityDistribution(hamiltonian,
                params, )*/
    }
}
