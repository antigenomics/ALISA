package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.algebra.matrix.Matrix;
import com.antigenomics.alisa.encoding.OneLayerState;
import com.antigenomics.alisa.encoding.TwoLayerState;
import com.antigenomics.alisa.entities.BitString;
import com.antigenomics.alisa.impl.bitstring.BitStringOneHotEncoder;
import com.antigenomics.alisa.impl.bitstring.BitStringSpinEncoder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoLayerSpinGlassHamiltonianTest {
    @Test
    public void instanceTest() {
        BitStringOneHotEncoder bitStringSpinEncoder = new BitStringOneHotEncoder(2);
        TwoLayerSpinGlassHamiltonian hamiltonian = new TwoLayerSpinGlassHamiltonian(bitStringSpinEncoder,
                bitStringSpinEncoder);

        BitString s11 = new BitString(new boolean[]{true, true}),
                s01 = new BitString(new boolean[]{false, true}),
                s10 = new BitString(new boolean[]{true, false}),
                s00 = new BitString(new boolean[]{false, false});

        Matrix H1 = Matrix.DENSE(new double[][]{{1, 0}, {0, 1}}),
                H2 = Matrix.DENSE(new double[][]{{1, 0}, {0, 1}}),
                H12 = Matrix.DENSE(new double[][]{{0, 2}, {3, 0}});

        LinearSpaceObjectArray params = new LinearSpaceObjectArray(Arrays.asList(H1, H2, H12));

        assertEquals(2 + 2 + (2+3), hamiltonian.computeEnergy(new TwoLayerState(s11, s11), params));
        assertEquals(2, hamiltonian.computeEnergy(new TwoLayerState(s10, s10), params));
        assertEquals(2, hamiltonian.computeEnergy(new TwoLayerState(s01, s01), params));
        assertEquals(0, hamiltonian.computeEnergy(new TwoLayerState(s00, s00), params));
    }
}
