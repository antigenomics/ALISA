package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.matrix.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpeedTest {
    @Test
    void spraseDenseOuterProductSum() {
        final Random rnd = new Random(42);
        List<Vector> dense = new ArrayList<>(),
                sparse = new ArrayList<>();

        int ndim = 50, nvec = 1000;
        double f = 0.1;

        System.out.println("Sparse vs dense vector test. Vector length='" + ndim +
                "'; Number of vectors='" + nvec + "'; Sparsity='" + f + "'. Preparing random dataset.");

        for (int i = 0; i < nvec; i++) {
            double[] arr = new double[ndim];
            List<IndexedVectorValue> values = new ArrayList<>();

            for (int j = 0; j < ndim; j++) {
                if (rnd.nextDouble() < f) {
                    arr[j] = 1;
                    values.add(new IndexedVectorValue(j, 1.0));
                }
            }

            dense.add(new DenseVector(arr));
            sparse.add(new SparseVector(values, ndim));
        }

        long startTime;
        Matrix input;

        ///

        input = Matrix.DENSE_ZEROS(ndim, ndim);
        startTime = System.currentTimeMillis();
        for (Vector v : dense) {
            for (Vector v2 : dense) {
                input.addInplace(v.outerProduct(v2));
            }
        }
        long timeDenseOuterProductSum = System.currentTimeMillis() - startTime;
        System.out.println("Dense vector a and b (all combinations) -> dense storage += a*bT: "
                + timeDenseOuterProductSum + "ms");

        //////

        input = Matrix.DENSE_ZEROS(ndim, ndim);
        startTime = System.currentTimeMillis();
        for (Vector v : sparse) {
            for (Vector v2 : sparse) {
                input.addInplace(v.outerProduct(v2));
            }
        }
        long timeSparseOuterProductSum = System.currentTimeMillis() - startTime;
        System.out.println("Sparse vector a and b (all combinations) -> dense storage += a*bT: " +
                timeSparseOuterProductSum + "ms");

        assertTrue(timeDenseOuterProductSum > 3 * timeSparseOuterProductSum);

        ///

        startTime = System.currentTimeMillis();
        for (Vector v : dense) {
            v.expand().norm2();
        }
        long timeDenseExpandSum = System.currentTimeMillis() - startTime;
        System.out.println("Dense vector a -> dense storage -> norm2: " +
                timeDenseExpandSum + "ms");

        //////

        startTime = System.currentTimeMillis();
        for (Vector v : sparse) {
            v.expand().norm2();
        }
        long timeSparseExpandSum = System.currentTimeMillis() - startTime;
        System.out.println("Sparse vector a -> sparse storage -> norm2: " +
                timeSparseExpandSum + "ms");

        assertTrue(timeDenseExpandSum > 2 * timeSparseExpandSum);
    }
}
