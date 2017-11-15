package com.antigenomics.alisa.algebra;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DenseVectorTest {
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
    public void miscTest() {
        assertEquals(10, Vector.ones(10).norm1());
        assertEquals(20, Vector.ones(100).multiply(2.0).norm2());
    }

    @Test
    public void linearOperationsTest() {
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
        Vector v1 = Vector.fromValues(1, 20, 3, -4, 1.1231232e-10);
        Vector v2 = v1.deepCopy();

        v1.multiplyInplace(0);
        assertEquals(Vector.zeros(v1.length), v1);

        v1.addInplace(v2);
        assertEquals(v2, v1);

        v1.addInplace(v2);
        System.out.println(v1);
        assertEquals(v2.multiply(2), v1);
    }

    @Test
    public void algebraTest() {
        Vector v1 = Vector.fromValues(1, -2, 0, -3, 4, 10, 12, 100, -10);
        assertEquals(v1.norm2() * v1.norm2(),
                v1.dotProduct(v1));
    }
}
