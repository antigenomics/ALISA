package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.algebra.matrix.Matrix;
import com.antigenomics.alisa.encoding.ThreeLayerState;
import com.antigenomics.alisa.entities.BitString;
import com.antigenomics.alisa.impl.bitstring.BitStringOneHotEncoder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreeLayerSpinGlassHamiltonianTest {
    @Test
    public void instanceTest() {
        BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(2);
        ThreeLayerSpinGlassHamiltonian hamiltonian = new ThreeLayerSpinGlassHamiltonian(bitStringSpinEncoder,
                bitStringSpinEncoder, bitStringSpinEncoder);

        BitString s11 = new BitString(new boolean[]{true, true}),
                s01 = new BitString(new boolean[]{false, true}),
                s10 = new BitString(new boolean[]{true, false}),
                s00 = new BitString(new boolean[]{false, false});

        Matrix H1 = Matrix.DENSE(new double[][]{{1, 0}, {0, 1}}),
                H2 = Matrix.DENSE(new double[][]{{1, 0}, {0, 1}}),
                H3 = Matrix.DENSE(new double[][]{{1, 0}, {0, 1}}),
                H12 = Matrix.DENSE(new double[][]{{0, 2}, {3, 0}}),
                H23 = Matrix.DENSE(new double[][]{{0, 2}, {3, 0}}),
                H13 = Matrix.DENSE(new double[][]{{0, 2}, {3, 0}});

        LinearSpaceObjectArray params = new LinearSpaceObjectArray(Arrays.asList(H1, H2, H3, H12, H23, H13));

        assertEquals(2 + 2 + 2 + 3*(2+3), hamiltonian.computeEnergy(new ThreeLayerState(s11, s11, s11), params));
        assertEquals(3, hamiltonian.computeEnergy(new ThreeLayerState(s10, s10, s10), params));
        assertEquals(3, hamiltonian.computeEnergy(new ThreeLayerState(s01, s01, s01), params));
        assertEquals(0, hamiltonian.computeEnergy(new ThreeLayerState(s00, s00, s00), params));
    }
}
