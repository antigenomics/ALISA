package com.antigenomics.alisa.algebra.matrix;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.equalUpToTol;
import static org.junit.jupiter.api.Assertions.*;

public class SparseVectorTest {
    @Test
    void instanceTest() {
        List<IndexedVectorValue> valueList = new ArrayList<>();
        valueList.add(new IndexedVectorValue(0, 1));
        valueList.add(new IndexedVectorValue(1, 2));
        valueList.add(new IndexedVectorValue(2, 0));
        valueList.add(new IndexedVectorValue(3, 3));
        valueList.add(new IndexedVectorValue(4, 4));
        assertEquals(
                Vector.SPARSE(1, 2, 0, 3, 4),
                new SparseVector(valueList, 5, true));
        //assertEquals(
        //        Vector.SPARSE(1, 2, 0, 3, 4),
        //        new SparseVector(valueList, 5, false));
        assertEquals(
                Vector.SPARSE(1, 2, 0, 3, 4).getEffectiveSize(),
                4);
        assertEquals(
                Vector.SPARSE(1, 2, 0, 3, 4).length,
                5);
    }


    @Test
    void safetyTest() {
        // incorrect length
        List<IndexedVectorValue> valueList = new ArrayList<>();
        valueList.add(new IndexedVectorValue(0, 1));
        valueList.add(new IndexedVectorValue(1, 2));
        assertThrows(Exception.class, () -> new SparseVector(valueList, 1, true));

        // modification/copy
        List<IndexedVectorValue> valueList2 = new ArrayList<>();
        valueList2.add(new IndexedVectorValue(0, 1));
        valueList2.add(new IndexedVectorValue(1, 2));
        valueList2.add(new IndexedVectorValue(10, 1));
        SparseVector v1 = new SparseVector(valueList2, 11);
        valueList2.add(new IndexedVectorValue(5, 1));
        valueList2.add(new IndexedVectorValue(6, 2));
        SparseVector v2 = new SparseVector(valueList2, 11, true);
        assertNotEquals(v2, v1);
        Vector v3 = v2.deepCopy();
        v2.addInplace(v1);
        assertEquals(v2, v3.add(v1));

        // dimension match
        SparseVector v4 = new SparseVector(valueList, 2),
                v5 = new SparseVector(valueList, 3); // should work with missing elements
        assertThrows(Exception.class, () -> v4.add(v5));
        assertThrows(Exception.class, () -> v4.addInplace(v5));

        // sort test
        valueList.add(new IndexedVectorValue(4, 1));
        valueList.add(new IndexedVectorValue(2, 1));

        IndexedVectorValue prevValue = IndexedVectorValue.EMPTY;

        for (IndexedVectorValue v : new SparseVector(valueList, 5, true)) {
            assertTrue(v.compareTo(prevValue) > 0);
            prevValue = v;
        }
    }

    @Test
    void normTest() {
        assertEquals(0, Vector.SPARSE_ZEROS(10).norm1());
        assertEquals(10, Vector.SPARSE(1,1,1,1,1,1,1,1,1,1).norm1());
        assertEquals(2, Vector.SPARSE_ONEHOT(5, 10).multiply(2.0).norm2());
    }

    @Test
    void linearOperationsTest() {
        // commutative
        Vector v1 = Vector.SPARSE(0, 7, 1, -2, 100),
                v2 = Vector.SPARSE(0, 7, 100, 0.1, -10);

        assertTrue(v1.add(v2).equals(v2.add(v1)));

        assertEquals(
                Vector.SPARSE(6, 3, 5, 7, 9),
                Vector.SPARSE(5, 1, 2, 3, 4).add(Vector.SPARSE(1, 2, 3, 4, 5))
        );

        assertEquals(
                Vector.SPARSE(2.5, 0.5, 1, 1.5, 2),
                Vector.SPARSE(5, 1, 2, 3, 4).multiply(0.5)
        );
    }

    @Test
    void linearOperationsInplaceTest() {
        Vector v1 = Vector.SPARSE(1, 20, -3, 4, 1.1231232e-10);
        Vector v2 = v1.deepCopy();

        // mul

        v1.multiplyInplace(0);
        assertTrue(equalUpToTol(Vector.DENSE_ZEROS(v1.length), v1));

        // add inplace

        v1.addInplace(v2);
        assertEquals(v2, v1);

        v1.addInplace(v1);
        assertEquals(v2.multiply(2), v1);
    }

    @Test
    void algebraTest() {
        // dot product

        Vector v1 = Vector.SPARSE(1, -2, 0, -3, 4, 10, 12, 100, -10);
        assertEquals(v1.norm2() * v1.norm2(),
                v1.dotProduct(v1));

        // cartesian product

        Vector v3 = Vector.SPARSE(1, 2, 3, 4),
                v4 = Vector.SPARSE(3, 2, 1),
                v5 = Vector.SPARSE(0, 2, 1);

        // v3 v4T

        Matrix v3v4op = v3.outerProduct(v4);

        assertEquals(Matrix.SPARSE(new double[][]{
                        {3, 2, 1},
                        {6, 4, 2},
                        {9, 6, 3},
                        {12, 8, 4}
                }),
                v3v4op);

        assertFalse(v3v4op.isLowerTriangular);

        // v3 v5T

        Matrix v3v5op = v3.outerProduct(v5);

        assertEquals(Matrix.SPARSE(new double[][]{
                        {0, 2, 1},
                        {0, 4, 2},
                        {0, 6, 3},
                        {0, 8, 4}
                }),
                v3v5op);

        assertFalse(v3v5op.isLowerTriangular);

        // v3 v3T

        Matrix v4exp = v4.expand();

        assertTrue(equalUpToTol(Matrix.SPARSE(new double[][]{
                        {9, 6, 3},
                        {6, 4, 2},
                        {3, 2, 1}
                }),
                v4exp));

        assertTrue(v4exp.isLowerTriangular);
    }

    @Test
    void conversionTest() {
        Vector v1 = Vector.SPARSE(1, -2, 0, -3, 4, 10, 12, 100, -10);

        assertTrue(equalUpToTol(v1, v1.asDense()));
        assertTrue(equalUpToTol(v1, v1.asSparse()));
    }
}
