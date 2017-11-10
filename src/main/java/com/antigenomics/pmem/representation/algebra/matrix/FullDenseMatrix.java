package com.antigenomics.pmem.representation.algebra.matrix;

import com.antigenomics.pmem.representation.MutableLinearSpaceObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class FullDenseMatrix
        extends DenseMatrix {
    private List<RealMatrixElement> elementList = null;

    public FullDenseMatrix(double[] elements, int numberOfColumns) {
        super(elements, new DenseMatrixStorageIndex(elements.length, numberOfColumns));
    }

    @Override
    protected RealMatrix plusUnchecked(RealMatrix a) {
        double[] newElements = Arrays.copyOf(elements, elements.length);

        int k = 0;
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j < getNumberOfColumns(); j++) {
                newElements[k] += a.getAt(i, j);
                k++;
            }
        }

        return new FullDenseMatrix(newElements, getNumberOfColumns());
    }

    @Override
    public boolean isStrictlySymmetric() {
        return false;
    }

    @Override
    public RealMatrix multiply(double scalar) {
        double[] newElements = Arrays.copyOf(elements, elements.length);

        for (int i = 0; i < elements.length; i++) {
            newElements[i] *= scalar;
        }

        return new FullDenseMatrix(newElements, getNumberOfColumns());
    }

    @Override
    public MutableLinearSpaceObject<RealMatrix> toMutable() {
        return new FullMutableMatrix(Arrays.copyOf(elements, elements.length),
                getNumberOfColumns());
    }

    @Override
    public Iterator<RealMatrixElement> iterator() {
        if (elementList == null) {
            elementList = new ArrayList<>();
            int k = 0;
            for (int i = 0; i < getNumberOfRows(); i++) {
                for (int j = 0; j < getNumberOfColumns(); j++) {
                    elementList.add(new RealMatrixElement(i, j, elements[k]));
                    k++;
                }
            }
        }

        return elementList.iterator();
    }

    public static final class DenseMatrixStorageIndex
            implements MatrixStorageIndex {
        private final int numberOfColumns, numberOfRows;

        public DenseMatrixStorageIndex(int numberOfElements, int numberOfColumns) {
            if (numberOfElements % numberOfColumns != 0) {
                throw new IllegalArgumentException("Number of elements should be a multiple of number of columns.");
            }

            this.numberOfColumns = numberOfColumns;
            this.numberOfRows = numberOfElements / numberOfColumns;
        }

        @Override
        public int getIndex(int i, int j) {
            return i * numberOfColumns + j;
        }

        @Override
        public int getNumberOfColumns() {
            return numberOfColumns;
        }

        @Override
        public int getNumberOfRows() {
            return numberOfRows;
        }
    }
}
