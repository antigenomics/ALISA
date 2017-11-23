package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.computeNumberOfFullMatrixRows;

public final class SparseTriangularMatrix extends SparseMatrix {
    protected SparseTriangularMatrix(List<IndexedMatrixValue> elementList,
                                     int size) {
        this(elementList, size, false);
    }

    protected SparseTriangularMatrix(double[] elements, int size) {
        super(new LinkedList<>(), size, size, false, false);
        assert elements.length == LinearAlgebraUtils.getTriangularMatrixLength(size);
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j <= i; j++) {
                double value = elements[k++];
                if (value != 0) {
                    elementList.add(new IndexedMatrixValue(i, j, value));
                }
            }
        }
    }

    protected SparseTriangularMatrix(double[][] elements) {
        super(new LinkedList<>(), elements.length, elements[0].length, false, true);
        assert elements.length == elements[0].length;
        for (int i = 0; i < numberOfRows; i++) {
            double[] row = elements[i];
            for (int j = 0; j <= i; j++) {
                double value = row[j];
                if (value != 0) {
                    elementList.add(new IndexedMatrixValue(i, j, value));
                }
            }
        }
    }

    public SparseTriangularMatrix(List<IndexedMatrixValue> elementList,
                                  int size, boolean safe) {
        super(elementList, size, size, safe, true);
    }

    @Override
    protected Matrix withElements(List<IndexedMatrixValue> elementList) {
        return new SparseTriangularMatrix(elementList, numberOfRows);
    }
}
