package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.MutableLinearSpaceObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class LowerTriangularDenseMatrix
        extends DenseMatrix {
    private List<RealMatrixElement> elementList = null;

    public LowerTriangularDenseMatrix(double[] elements) {
        super(elements, new LowerTriangularMatrixLinearIndexing(elements.length));
    }

    @Override
    protected RealMatrix plusUnchecked(RealMatrix a) {
        if (!a.isStrictlySymmetric()) {
            return a.plus(this);
        } else {
            double[] newElements = Arrays.copyOf(elements, elements.length);

            int k = 0;
            for (int i = 0; i < getNumberOfRows(); i++) {
                for (int j = 0; j <= i; j++) {
                    newElements[k] += a.getAt(i, j);
                    k++;
                }
            }

            return new LowerTriangularDenseMatrix(newElements);
        }
    }

    @Override
    public boolean isStrictlySymmetric() {
        return true;
    }

    @Override
    public RealMatrix multiply(double scalar) {
        double[] newElements = Arrays.copyOf(elements, elements.length);

        for (int i = 0; i < elements.length; i++) {
            newElements[i] *= scalar;
        }

        return new LowerTriangularDenseMatrix(newElements);
    }

    @Override
    public MutableLinearSpaceObject<RealMatrix> toMutable() {
        return new LowerTriangularMutableMatrix(elements);
    }

    @Override
    public Iterator<RealMatrixElement> iterator() {
        if (elementList == null) {
            elementList = new ArrayList<>();
            int k = 0;
            for (int i = 0; i < getNumberOfRows(); i++) {
                for (int j = 0; j <= i; j++) {
                    elementList.add(new RealMatrixElement(i, j, elements[k]));
                    k++;
                }
            }
        }

        return elementList.iterator();
    }

    public static final class LowerTriangularMatrixLinearIndexing
            implements MatrixLinearIndexing {
        private final int n;

        public LowerTriangularMatrixLinearIndexing(int numberOfElements) {
            this.n = getN(numberOfElements);

            if (getLength(n) == numberOfElements) {
                throw new IllegalArgumentException("Wrong number of elements.");
            }
        }

        private int getLength(int n) {
            return (n * (n + 1)) / 2;
        }

        private int getN(int length) {
            return (int) ((-1 + Math.sqrt(1 + 8 * length)) / 2);
        }

        @Override
        public int getIndex(int i, int j) {
            if (j > i) {
                return getIndex(j, i);
            } else {
                return getLength(i) + j;
            }
        }

        @Override
        public int getNumberOfRows() {
            return n;
        }

        @Override
        public int getNumberOfColumns() {
            return n;
        }
    }
}