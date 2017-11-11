package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.LinearSpaceObjectUtils;
import com.antigenomics.alisa.representation.MutableLinearSpaceObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class FullDenseMatrix
        extends DenseMatrix {
    private List<RealMatrixElement> elementList = null;

    public FullDenseMatrix(double[] elements, int numberOfColumns) {
        super(elements, new FullMatrixIndexing(elements.length, numberOfColumns));
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
    public RealMatrix multiply(double scalar) {
        return new FullDenseMatrix(LinearSpaceObjectUtils.scale(elements, scalar),
                getNumberOfColumns());
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
}
