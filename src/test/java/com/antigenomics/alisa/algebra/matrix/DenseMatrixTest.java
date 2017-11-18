package com.antigenomics.alisa.algebra.matrix;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DenseMatrixTest {
    @Test
    void instanceTest() {
        assertEquals(
                Matrix.DENSE(new double[][]{{1, 0, 0}, {2, 3, 0}, {4, 5, 6}}),
                new DenseMatrix(new double[]{1, 0, 0, 2, 3, 0, 4, 5, 6}, 3, true));

        assertEquals(
                Matrix.DENSE(new double[][]{{1, 0, 0}, {2, 3, 0}, {4, 5, 6}}),
                new DenseMatrix(new double[]{1, 0, 0, 2, 3, 0, 4, 5, 6}, 3, false));

        List<IndexedMatrixValue> valueList = new ArrayList<>();
        valueList.add(new IndexedMatrixValue(0, 0, 1.0));
        valueList.add(new IndexedMatrixValue(1, 0, 2.0));
        valueList.add(new IndexedMatrixValue(2, 0, 3.0));
        valueList.add(new IndexedMatrixValue(0, 1, 4.0));
        valueList.add(new IndexedMatrixValue(1, 1, 5.0));
        valueList.add(new IndexedMatrixValue(2, 1, 6.0));

        assertEquals(
                Matrix.DENSE(new double[][]{{1, 4, 0}, {2, 5, 0}, {3, 6, 0}}),
                new DenseMatrix(valueList, 3, 3));

        assertEquals(
                new DenseMatrix(new double[]{1, 0, 0, 2, 3, 0, 4, 5, 6, 1, 2, 3},
                        3, false).getEffectiveSize(),
                9);

        assertEquals(
                new DenseMatrix(new double[]{1, 0, 0, 2, 3, 0, 4, 5, 6, 1, 2, 3, 4, 5, 6},
                        3, false).getNumberOfRows(),
                5);
    }

    @Test
    void safetyTest() {
        // out of bounds

        assertThrows(Exception.class, () -> new DenseMatrix(new double[3], 2));

        List<IndexedMatrixValue> valueList = new ArrayList<>();
        valueList.add(new IndexedMatrixValue(0, 0, 1));
        valueList.add(new IndexedMatrixValue(0, 1, 1));
        valueList.add(new IndexedMatrixValue(1, 0, 1));
        valueList.add(new IndexedMatrixValue(1, 1, 1));
        assertThrows(Exception.class, () -> new DenseMatrix(valueList, 1, 2));

        // modification

        double[] values = new double[9];
        DenseMatrix v1 = new DenseMatrix(values, 3);
        DenseMatrix v2 = new DenseMatrix(values, 3, true);
        values[1] = values[5] = values[7] = 1;
        assertNotEquals(v2, v1);
        Matrix v3 = v2.deepCopy();
        v2.addInplace(v1);
        assertEquals(v2, v3.add(v1));

        // dimension match

        DenseMatrix v4 = new DenseMatrix(valueList, 2, 2),
                v5 = new DenseMatrix(valueList, 2, 3); // should work with missing elements
        assertThrows(Exception.class, () -> v4.add(v5));
        assertThrows(Exception.class, () -> v4.addInplace(v5));

        // ordering test

        valueList.add(new IndexedMatrixValue(3, 4, 1));
        valueList.add(new IndexedMatrixValue(2, 2, 1));

        IndexedMatrixValue prevValue = IndexedMatrixValue.EMPTY;

        for (IndexedMatrixValue v : new DenseMatrix(valueList, 5, 5)) {
            assertTrue(v.compareTo(prevValue) > 0);
            prevValue = v;
        }
    }
}
