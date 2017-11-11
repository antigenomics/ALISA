package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.MutableLinearSpaceObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class LowerTriangularDenseMatrix
        extends DenseMatrix {
    private List<RealMatrixElement> elementList = null;

    public LowerTriangularDenseMatrix(double[] elements) {
        super(elements, new LowerTriangularMatrixIndexing(elements.length));
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
}