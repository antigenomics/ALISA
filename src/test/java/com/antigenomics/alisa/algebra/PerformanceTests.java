package com.antigenomics.alisa.algebra;

import com.antigenomics.alisa.estimator.mc.MonteCarloUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PerformanceTests {
    @Test
    public void spraseDenseOuterProductSum() {
        List<Vector> dense = new ArrayList<>(),
                sparse = new ArrayList<>();

        int ndim = 50, nvec = 500;
        double f = 0.1;

        for (int i = 0; i < nvec; i++) {
            double[] arr = new double[ndim];
            List<IndexedVectorValue> values = new ArrayList<>();

            for (int j = 0; j < ndim; j++) {
                if (MonteCarloUtils.nextDouble() < f) {
                    arr[j] = 1;
                    values.add(new IndexedVectorValue(j, 1.0));
                }
            }

            dense.add(new DenseVector(arr));
            sparse.add(new SparseVector(values, ndim));
        }

        long startTime, estimatedTime;
        Matrix input;

        ///

        input = new DenseMatrix(new double[ndim * ndim], ndim);
        startTime = System.currentTimeMillis();
        for (Vector v : dense) {
            for (Vector v2 : dense) {
                input.addInplace(v.outerProduct(v2));
            }
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
        //System.out.println(input.multiply(1.0 / nvec / nvec / f / f));

        //////

        input = new DenseMatrix(new double[ndim * ndim], ndim);
        startTime = System.currentTimeMillis();
        for (Vector v : sparse) {
            for (Vector v2 : sparse) {
                input.addInplace(v.outerProduct(v2));
            }
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
        //System.out.println(input.multiply(1.0 / nvec / nvec / f / f));

        ///

        startTime = System.currentTimeMillis();
        for (Vector v : dense) {
            v.expand().norm2();
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);

        //////

        startTime = System.currentTimeMillis();
        for (Vector v : sparse) {
            v.expand().norm2();
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
    }
}
