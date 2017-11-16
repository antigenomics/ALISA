package com.antigenomics.alisa.algebra;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.antigenomics.alisa.algebra.AlgebraTestUtil.assertLSOValuesEqual;
import static org.junit.jupiter.api.Assertions.*;

public class DenseVectorTest {
    @Test
    public void safetyTest() {
        // incorrect length
        List<IndexedVectorValue> valueList = new ArrayList<>();
        valueList.add(new IndexedVectorValue(0, 1));
        valueList.add(new IndexedVectorValue(1, 2));
        assertThrows(Exception.class, () -> new DenseVector(valueList, 1));

        // modification/copy
        double[] values = new double[10];
        DenseVector v1 = new DenseVector(values);
        DenseVector v2 = new DenseVector(values, true);
        values[1] = values[5] = values[7] = 1;
        assertNotEquals(v2, v1);
        Vector v3 = v2.deepCopy();
        v2.addInplace(v1);
        assertEquals(v2, v3.add(v1));

        // dimension match
        DenseVector v4 = new DenseVector(valueList, 2),
                v5 = new DenseVector(valueList, 3); // should work with missing elements
        assertThrows(Exception.class, () -> v4.add(v5));
        assertThrows(Exception.class, () -> v4.addInplace(v5));
    }

    @Test
    public void instanceTest() {
        assertEquals(
                Vector.fromValues(1, 2, 0, 3, 4),
                new DenseVector(new double[]{1, 2, 0, 3, 4}, true));
        assertEquals(
                Vector.fromValues(1, 2, 0, 3, 4),
                new DenseVector(new double[]{1, 2, 0, 3, 4}, false));
        List<IndexedVectorValue> valueList = new ArrayList<>();
        valueList.add(new IndexedVectorValue(0, 1));
        valueList.add(new IndexedVectorValue(1, 2));
        valueList.add(new IndexedVectorValue(2, 0));
        valueList.add(new IndexedVectorValue(3, 3));
        valueList.add(new IndexedVectorValue(4, 4));
        assertEquals(
                Vector.fromValues(1, 2, 0, 3, 4),
                new DenseVector(valueList, 5));
        assertEquals(
                Vector.fromValues(1, 2, 0, 3, 4).getEffectiveSize(),
                4);
        assertEquals(
                Vector.fromValues(1, 2, 0, 3, 4).length,
                5);
    }

    @Test
    public void normTest() {
        assertEquals(10, Vector.ones(10).norm1());
        assertEquals(20, Vector.ones(100).multiply(2.0).norm2());
    }

    @Test
    public void linearOperationsTest() {
        // commutative
        Vector v1 = Vector.fromValues(0, 7, 1, -2, 100),
                v2 = Vector.fromValues(0, 7, 100, 0.1, -10);

        assertTrue(v1.add(v2).equals(v2.add(v1)));

        assertEquals(
                Vector.fromValues(6, 3, 5, 7, 9),
                Vector.fromValues(5, 1, 2, 3, 4).add(Vector.fromValues(1, 2, 3, 4, 5))
        );

        assertEquals(
                Vector.fromValues(2.5, 0.5, 1, 1.5, 2),
                Vector.fromValues(5, 1, 2, 3, 4).multiply(0.5)
        );
    }

    @Test
    public void linearOperationsInplaceTest() {
        Vector v1 = Vector.fromValues(1, 20, -3, 4, 1.1231232e-10);
        Vector v2 = v1.deepCopy();

        // mul

        v1.multiplyInplace(0);
        assertLSOValuesEqual(Vector.zeros(v1.length), v1);

        // add inplace

        v1.addInplace(v2);
        assertEquals(v2, v1);

        v1.addInplace(v1);
        assertEquals(v2.multiply(2), v1);
    }

    @Test
    public void algebraTest() {
        // dot product

        Vector v1 = Vector.fromValues(1, -2, 0, -3, 4, 10, 12, 100, -10);
        assertEquals(v1.norm2() * v1.norm2(),
                v1.dotProduct(v1));

        // cartesian product

        Vector v3 = Vector.fromValues(1, 2, 3, 4),
                v4 = Vector.fromValues(3, 2, 1);

        // v3 v4T

        Matrix v3v4op = v3.outerProduct(v4);

        assertEquals(Matrix.fromArray(new double[][]{
                        {3, 2, 1},
                        {6, 4, 2},
                        {9, 6, 3},
                        {12, 8, 4}
                }),
                v3v4op);

        assertFalse(v3v4op.isLowerTriangular);

        // v3 v3T

        Matrix v4exp = v4.expand();

        assertLSOValuesEqual(Matrix.fromArray(new double[][]{
                        {9, 6, 3},
                        {6, 4, 2},
                        {3, 2, 1}
                }),
                v4exp);

        assertTrue(v4exp.isLowerTriangular);
    }

    @Test
    public void conversionTest() {
        Vector v1 = Vector.fromValues(1, -2, 0, -3, 4, 10, 12, 100, -10);

        assertLSOValuesEqual(v1, v1.asDense());
        assertLSOValuesEqual(v1, v1.asSparse());
    }
}
