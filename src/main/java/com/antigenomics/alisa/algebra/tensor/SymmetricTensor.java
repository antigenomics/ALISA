package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.*;

public final class SymmetricTensor extends Tensor {
    protected SymmetricTensor(double[] elements,
                              int size,
                              int numberOfCategories) {
        this(elements, size, numberOfCategories, false);
    }

    public SymmetricTensor(double[] elements,
                           int size,
                           int numberOfCategories,
                           boolean safe) {
        super(elements, size, size, numberOfCategories, numberOfCategories,
                true, true, safe);
    }

    protected SymmetricTensor(double[][][][] elements) {
        super(new double[getTriangularTensorLength(elements.length, elements[0][0].length)],
                elements.length, elements[0].length, elements[0][0].length, elements[0][0][0].length,
                false, true, false);
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            double[][][] r1 = elements[i];
            for (int j = 0; j <= i; j++) {
                double[][] c1 = r1[i];
                for (int a = 0; a < numberOfCategoryRows; a++) {
                    double[] a1 = c1[i];
                    for (int b = 0; b <= a; b++) {
                        this.elements[k++] = a1[b];
                    }
                }
            }
        }
    }

    public SymmetricTensor(Iterable<IndexedTensorValue> elements,
                           int size,
                           int numberOfCategories) {
        this(new double[getTriangularTensorLength(size, numberOfCategories)],
                size,
                numberOfCategories);
        for (IndexedTensorValue e : elements) {
            int fullMatrixIndex = getLinearIndex(e.getRowIndex(), e.getColumnIndex(),
                    e.getFirstCategoryIndex(), e.getSecondCategoryIndex());
            this.elements[fullMatrixIndex] = e.getDoubleValue();
        }
    }

    @Override
    protected int getLinearIndex(int rowIndex, int columnIndex, int rowCategory, int columnCategory) {
        return LinearAlgebraUtils.getTriangularTensorIndex(rowIndex, columnIndex, rowCategory, columnCategory,
                numberOfCategoryColumns);
    }

    @Override
    protected Tensor withElements(double[] elements) {
        return new SymmetricTensor(elements,
                numberOfRows, numberOfCategoryRows);
    }

    @Override
    public int getEffectiveSize() {
        int effectiveSize = 0;
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j <= i; j++) {
                for (int a = 0; a < numberOfCategoryRows; a++) {
                    for (int b = 0; b <= a; b++) {
                        if (elements[k++] != 0) {
                            effectiveSize++;
                        }
                    }
                }
            }
        }
        return effectiveSize;
    }

    @Override
    public Iterator<IndexedTensorValue> iterator() {
        List<IndexedTensorValue> values = new LinkedList<>();

        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j <= i; j++) {
                for (int a = 0; a < numberOfCategoryRows; a++) {
                    for (int b = 0; b <= a; b++) {
                        values.add(new IndexedTensorValue(i, j, a, b,
                                elements[k++]));
                    }
                }
            }
        }
        return values.iterator();
    }
}
