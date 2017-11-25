package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.getSemiTriangularTensorLength;

public final class SemiSymmetricTensor extends Tensor {
    protected SemiSymmetricTensor(double[] elements,
                                  int numberOfRows, int numberOfColumns,
                                  int numberOfCategories) {
        this(elements, numberOfRows, numberOfColumns, numberOfCategories, false);
    }

    public SemiSymmetricTensor(double[] elements,
                               int numberOfRows, int numberOfColumns,
                               int numberOfCategories,
                               boolean safe) {
        super(elements, numberOfRows, numberOfColumns, numberOfCategories, numberOfCategories,
                false, true, safe);
    }

    protected SemiSymmetricTensor(double[][][][] elements) {
        super(new double[getSemiTriangularTensorLength(elements.length, elements[0].length, elements[0][0].length)],
                elements.length, elements[0].length, elements[0][0].length, elements[0][0][0].length,
                false, true, false);
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            double[][][] r1 = elements[i];
            for (int j = 0; j < numberOfColumns; j++) {
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

    public SemiSymmetricTensor(Iterable<IndexedTensorValue> elements,
                               int numberOfRows, int numberOfColumns,
                               int numberOfCategories) {
        this(new double[getSemiTriangularTensorLength(numberOfRows, numberOfColumns, numberOfCategories)],
                numberOfRows, numberOfColumns,
                numberOfCategories);
        for (IndexedTensorValue e : elements) {
            int fullMatrixIndex = getLinearIndex(e.getRowIndex(), e.getColumnIndex(),
                    e.getFirstCategoryIndex(), e.getSecondCategoryIndex());
            this.elements[fullMatrixIndex] = e.getDoubleValue();
        }
    }

    @Override
    protected int getLinearIndex(int rowIndex, int columnIndex, int rowCategory, int columnCategory) {
        return LinearAlgebraUtils.getSemiTriangularTensorIndex(rowIndex, columnIndex, rowCategory, columnCategory,
                numberOfColumns, numberOfCategoryColumns);
    }

    @Override
    protected Tensor withElements(double[] elements) {
        return new SemiSymmetricTensor(elements,
                numberOfRows, numberOfColumns, numberOfCategoryRows);
    }

    @Override
    public int getEffectiveSize() {
        int effectiveSize = 0;
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
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
            for (int j = 0; j < numberOfColumns; j++) {
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
