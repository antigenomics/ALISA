package com.antigenomics.alisa.statistics;

import com.antigenomics.alisa.algebra.matrix.Matrix;
import com.antigenomics.alisa.encoding.BitStringOneHotEncoder;
import com.antigenomics.alisa.entities.StateString;
import com.antigenomics.alisa.hamiltonian.MatrixHamiltonian;
import com.antigenomics.alisa.hamiltonian.SingleLayerSpinGlassHamiltonian;
import com.antigenomics.alisa.state.OneLayerState;
import com.antigenomics.alisa.state.SimpleOneLayerStateStringSequence;
import com.antigenomics.alisa.state.StateSequence;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExactStateProbabilityDistributionTest {
    @Test
    @SuppressWarnings("unchecked")
    public void testNorm() {
        int len = 20;
        BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(len);
        SingleLayerSpinGlassHamiltonian hamiltonian = new SingleLayerSpinGlassHamiltonian(bitStringSpinEncoder);

        StateSequence ss = new SimpleOneLayerStateStringSequence(2, len);
        Matrix H = Matrix.DENSE_RANDOM(len, len).add(Matrix.DENSE_ONES(len, len).multiply(-0.5));

        //System.out.println(H);

        ExactStateProbabilityDistribution probs = new ExactStateProbabilityDistribution(hamiltonian,
                MatrixHamiltonian.asParameters(H), ss.asStateSpace());

        List states = (List) ss.stream().collect(Collectors.toList());


        assertEquals(1.0,
                probs.computeProbabilities(states).stream()
                        .mapToDouble(x -> ((StateProbability) x).getProbability()).sum(),
                1e-10);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDistribution1() {
        int len = 20;
        BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(len);
        SingleLayerSpinGlassHamiltonian hamiltonian = new SingleLayerSpinGlassHamiltonian(bitStringSpinEncoder);

        StateSequence ss = new SimpleOneLayerStateStringSequence(2, len);
        Matrix H = Matrix.DENSE_ZEROS(len, len);

        ExactStateProbabilityDistribution probs = new ExactStateProbabilityDistribution(hamiltonian,
                MatrixHamiltonian.asParameters(H), ss.asStateSpace());

        List states = (List) ss.stream().collect(Collectors.toList());

        probs.computeProbabilities(states).forEach(
                x -> assertEquals(1.0 / ss.getCharacteristicSize(), ((StateProbability) x).getProbability())
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDistribution2() {
        int len = 3;
        BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(len);
        SingleLayerSpinGlassHamiltonian hamiltonian = new SingleLayerSpinGlassHamiltonian(bitStringSpinEncoder);

        StateSequence ss = new SimpleOneLayerStateStringSequence(2, len);
        Matrix H = Matrix.DENSE(new double[][]{{100, 0, 0}, {0, 0, 0}, {0, 0, 0}});

        ExactStateProbabilityDistribution probs = new ExactStateProbabilityDistribution(hamiltonian,
                MatrixHamiltonian.asParameters(H), ss.asStateSpace());

        List states = (List) ss.stream().collect(Collectors.toList());

        probs.computeProbabilities(states).forEach(
                x -> {
                    StateProbability<OneLayerState<StateString>> sp = (StateProbability<OneLayerState<StateString>>) x;
                    assertEquals(sp.getState().getValue().getAt(0) == 0 ? 0 : 0.25, sp.getProbability(), 1e-20);
                }
        );
    }
}
