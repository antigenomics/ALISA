package com.antigenomics.alisa.algebra.matrix;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.equalUpToTol;
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
        DenseMatrix m1 = new DenseMatrix(values, 3);
        DenseMatrix m2 = new DenseMatrix(values, 3, true);
        values[1] = values[5] = values[7] = 1;
        assertNotEquals(m2, m1);
        Matrix m3 = m2.deepCopy();
        m2.addInplace(m1);
        assertEquals(m2, m3.add(m1));

        // dimension match

        DenseMatrix m4 = new DenseMatrix(valueList, 2, 2),
                m5 = new DenseMatrix(valueList, 2, 3); // should work with missing elements
        assertThrows(Exception.class, () -> m4.add(m5));
        assertThrows(Exception.class, () -> m4.addInplace(m5));

        // ordering test

        valueList.add(new IndexedMatrixValue(3, 4, 1));
        valueList.add(new IndexedMatrixValue(2, 2, 1));

        IndexedMatrixValue prevValue = IndexedMatrixValue.EMPTY;

        for (IndexedMatrixValue v : new DenseMatrix(valueList, 5, 5)) {
            assertTrue(v.compareTo(prevValue) > 0);
            prevValue = v;
        }
    }

    @Test
    void normTest() {
        assertEquals(0, Matrix.DENSE_ZEROS(10, 20).norm1());
        assertEquals(10, Matrix.DENSE_EYE(10).norm1());
        assertEquals(20, Matrix.DENSE_EYE(100).multiply(2.0).norm2());
    }

    @Test
    void linearOperationsTest() {
        // commutative
        Matrix m1 = Matrix.DENSE(new double[][]{{0, 7}, {1, -2}, {100, -4}}),
                m2 = Matrix.DENSE(new double[][]{{10, 2}, {-7, -2}, {0, 40}});

        assertTrue(m1.add(m2).equals(m2.add(m1)));


        assertEquals(
                Matrix.DENSE(new double[][]{{6, 3, 5, 7}, {9, 0, -1, 2}}),
                Matrix.DENSE(new double[][]{{5, 1, 2, 3}, {4, 0, -1, 1}})
                        .add(Matrix.DENSE(new double[][]{{1, 2, 3, 4}, {5, 0, 0, 1}}))
        );

        assertTrue(equalUpToTol(
                Matrix.DENSE(new double[][]{{6, 4, 5, 7}, {9, 0, -1, 2}}),
                Matrix.DENSE(new double[][]{{-3, -2, -2.5, -3.5}, {-4.5, 0, 0.5, -1}}).multiply(-2)
        ));
    }

    @Test
    void linearOperationsInplaceTest() {
        Matrix m1 = Matrix.DENSE(new double[][]{{0, 7}, {1, -2}, {100, -4}});
        Matrix m2 = m1.deepCopy();

        // mul

        m1.multiplyInplace(0);
        assertTrue(equalUpToTol(Matrix.DENSE_ZEROS(m1.numberOfRows, m1.numberOfColumns), m1));

        // add inplace

        m1.addInplace(m2);
        assertEquals(m2, m1);

        m1.addInplace(m1);
        assertEquals(m2.multiply(2), m1);
    }

    @Test
    void algebraTest() {
        // dot product

        Vector v1 = Vector.DENSE(1, -2, 0, -3, 4.2, 10, 12.1, 100.01, -10),
                v2 = Vector.DENSE(7, 14.5, 0, 5, 4.2, 0, 12, 0, 77.1);
        assertEquals(v1.norm2() * v1.norm2(),
                Matrix.DENSE_EYE(v1.length).bilinearForm(v1));
        assertEquals(v1.dotProduct(v2),
                // todo: have to convert to 'full' matrix, as non-symmetric bilinear form is not impl in triangular
                new DenseMatrix(Matrix.DENSE_EYE(v1.length), v1.length, v1.length)
                        .bilinearForm(v1, v2));

        /*
        // cartesian product

        Vector v3 = Vector.DENSE(1, 2, 3, 4),
                v4 = Vector.DENSE(3, 2, 1);

        // v3 v4T

        Matrix v3v4op = v3.outerProduct(v4);

        assertEquals(Matrix.DENSE(new double[][]{
                        {3, 2, 1},
                        {6, 4, 2},
                        {9, 6, 3},
                        {12, 8, 4}
                }),
                v3v4op);

        assertFalse(v3v4op.isLowerTriangular);

        // v3 v3T

        Matrix v4exp = v4.expand();

        assertTrue(equalUpToTol(Matrix.DENSE(new double[][]{
                        {9, 6, 3},
                        {6, 4, 2},
                        {3, 2, 1}
                }),
                v4exp));

        assertTrue(v4exp.isLowerTriangular);*/
    }

    @Test
    void conversionTest() {
        Matrix m1 = Matrix.DENSE(new double[][]{{1, -2, 0}, {-3, 4, 10}, {12, 100, -10}});

        assertTrue(equalUpToTol(m1, m1.asDense()));
        assertTrue(equalUpToTol(m1, m1.asSparse()));
    }
}
