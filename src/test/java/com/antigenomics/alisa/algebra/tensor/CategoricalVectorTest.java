package com.antigenomics.alisa.algebra.tensor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoricalVectorTest {
    private final CategoryWeightPair[] cwpArr = new CategoryWeightPair[]{
            new CategoryWeightPair(1),
            new CategoryWeightPair(3),
            new CategoryWeightPair(0),
            new CategoryWeightPair(5),
            new CategoryWeightPair(9)
    };

    @Test
    public void instanceTest() {
        CategoricalVector v1 = new CategoricalVector(new int[]{1, 3, 0, 5, 9}, 10, false);
        CategoricalVector v2 = new CategoricalVector(cwpArr, 10),
                v3 = new CategoricalVector(cwpArr, 11);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
    }

    @Test
    public void safetyTest() {
        assertThrows(Exception.class, () -> new CategoricalVector(cwpArr, 9, true));
    }
}
