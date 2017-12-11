package com.antigenomics.alisa.impl.bitstring;

import com.antigenomics.alisa.algebra.matrix.Vector;
import com.antigenomics.alisa.encoding.Encoder;
import com.antigenomics.alisa.entities.BitString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncoderTest {
    @Test
    public void test() {
        Encoder bitStringSpinEncoder = new BitStringSpinEncoder(2);

        BitString s11 = new BitString(new boolean[]{true, true}),
                s01 = new BitString(new boolean[]{false, true}),
                s10 = new BitString(new boolean[]{true, false}),
                s00 = new BitString(new boolean[]{false, false});

        assertEquals(bitStringSpinEncoder.encode(s11),
                Vector.DENSE(0.5, 0.5));
        assertEquals(bitStringSpinEncoder.encode(s10),
                Vector.DENSE(0.5, -0.5));
        assertEquals(bitStringSpinEncoder.encode(s01),
                Vector.DENSE(-0.5, 0.5));
        assertEquals(bitStringSpinEncoder.encode(s00),
                Vector.DENSE(-0.5, -0.5));

        bitStringSpinEncoder = new BitStringOneHotEncoder(2);

        assertEquals(bitStringSpinEncoder.encode(s11),
                Vector.SPARSE(1, 1));
        assertEquals(bitStringSpinEncoder.encode(s10),
                Vector.SPARSE(1, 0));
        assertEquals(bitStringSpinEncoder.encode(s01),
                Vector.SPARSE(0, 1));
        assertEquals(bitStringSpinEncoder.encode(s00),
                Vector.SPARSE(0, 0));
    }
}
