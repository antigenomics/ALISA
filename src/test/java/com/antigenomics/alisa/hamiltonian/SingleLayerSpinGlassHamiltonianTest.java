package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.algebra.matrix.Matrix;
import com.antigenomics.alisa.state.OneLayerState;
import com.antigenomics.alisa.entities.BitString;
import com.antigenomics.alisa.encoding.BitStringOneHotEncoder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleLayerSpinGlassHamiltonianTest {
    @SuppressWarnings("unchecked")
    @Test
    public void energyTest() {
        BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(2);
        SingleLayerSpinGlassHamiltonian hamiltonian = new SingleLayerSpinGlassHamiltonian(bitStringSpinEncoder);

        BitString s11 = new BitString(new int[]{1, 1}),
                s01 = new BitString(new int[]{0, 1}),
                s10 = new BitString(new int[]{1, 0}),
                s00 = new BitString(new int[]{0, 0});

        Matrix Hij = Matrix.DENSE(new double[][]{{1, 2}, {3, 4}});

        LinearSpaceObjectArray params = new LinearSpaceObjectArray(Collections.singletonList(Hij));

        assertEquals(1 + 2 + 3 + 4, hamiltonian.computeEnergy(new OneLayerState(s11), params));
        assertEquals(4, hamiltonian.computeEnergy(new OneLayerState(s01), params));
        assertEquals(1, hamiltonian.computeEnergy(new OneLayerState(s10), params));
        assertEquals(0, hamiltonian.computeEnergy(new OneLayerState(s00), params));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void gradientTest() {
        BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(2);
        SingleLayerSpinGlassHamiltonian hamiltonian = new SingleLayerSpinGlassHamiltonian(bitStringSpinEncoder);

        BitString s11 = new BitString(new int[]{1, 1}),
                s01 = new BitString(new int[]{0, 1}),
                s10 = new BitString(new int[]{1, 0}),
                s00 = new BitString(new int[]{0, 0});

        LinearSpaceObjectArray params = new LinearSpaceObjectArray(Collections.singletonList(bitStringSpinEncoder.getZero()));

        assertEquals(Matrix.SPARSE(new double[][]{{1, 1}, {1, 1}}),
                hamiltonian.computeGradient(new OneLayerState(s11), params).get(0));
        assertEquals(Matrix.SPARSE(new double[][]{{1, 0}, {0, 0}}),
                hamiltonian.computeGradient(new OneLayerState(s10), params).get(0));
        assertEquals(Matrix.SPARSE(new double[][]{{0, 0}, {0, 1}}),
                hamiltonian.computeGradient(new OneLayerState(s01), params).get(0));
        assertEquals(Matrix.SPARSE(new double[][]{{0, 0}, {0, 0}}),
                hamiltonian.computeGradient(new OneLayerState(s00), params).get(0));
    }
}
