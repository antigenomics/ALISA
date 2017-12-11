package com.antigenomics.alisa.hamiltonian;

import com.antigenomics.alisa.algebra.LinearSpaceObjectArray;
import com.antigenomics.alisa.algebra.matrix.Matrix;
import com.antigenomics.alisa.encoding.OneLayerState;
import com.antigenomics.alisa.entities.BitString;
import com.antigenomics.alisa.impl.bitstring.BitStringSpinEncoder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleLayerSpinGlassHamiltonianTest {
    @Test
    public void instanceTest() {
        BitStringSpinEncoder bitStringSpinEncoder = new BitStringSpinEncoder(2);
        SingleLayerSpinGlassHamiltonian hamiltonian = new SingleLayerSpinGlassHamiltonian(bitStringSpinEncoder);

        BitString s11 = new BitString(new boolean[]{true, true}),
                s01 = new BitString(new boolean[]{false, true}),
                s10 = new BitString(new boolean[]{true, false}),
                s00 = new BitString(new boolean[]{false, false});

        Matrix Hij = Matrix.DENSE(new double[][]{{1, 2}, {3, 4}});

        LinearSpaceObjectArray params = new LinearSpaceObjectArray(Arrays.asList(Hij));

        assertEquals(0.5*0.5*(1+2+3+4), hamiltonian.computeEnergy(new OneLayerState(s11), params));
        assertEquals(0.5*0.5*(1-2-3+4), hamiltonian.computeEnergy(new OneLayerState(s01), params));
        assertEquals(0.5*0.5*(1-2-3+4), hamiltonian.computeEnergy(new OneLayerState(s10), params));
        assertEquals(0.5*0.5*(1+2+3+4), hamiltonian.computeEnergy(new OneLayerState(s00), params));
    }
}
