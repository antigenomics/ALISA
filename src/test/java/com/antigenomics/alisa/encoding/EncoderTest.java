package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.algebra.matrix.Vector;
import com.antigenomics.alisa.entities.BitString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncoderTest {
    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        Encoder bitStringSpinEncoder = new BitStringOneHotEncoder(2);

        BitString s11 = new BitString(new int[]{1, 1}),
                s01 = new BitString(new int[]{0, 1}),
                s10 = new BitString(new int[]{1, 0}),
                s00 = new BitString(new int[]{0, 0});

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
