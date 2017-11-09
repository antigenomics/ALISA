package com.antigenomics.pmem.representation.algebra;

import com.antigenomics.pmem.representation.MutableLinearSpaceObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FullDenseMatrix
        extends DenseMatrix {
    private final int numberOfColumns, numberOfRows;
    private List<RealMatrixElement> elementList = null;

    public FullDenseMatrix(double[] elements, int numberOfColumns) {
        super(elements);

        if (elements.length % numberOfColumns != 0) {
            throw new IllegalArgumentException("Number of elements should be a multiple of number of columns.");
        }

        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = elements.length / numberOfColumns;
    }

    @Override
    protected int getIndex(int i, int j) {
        return i * numberOfColumns + j;
    }

    @Override
    protected RealMatrix plusUnchecked(RealMatrix a) {
        double[] newElements = Arrays.copyOf(elements, elements.length);

        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                newElements[k] += a.getAt(i, j);
                k++;
            }
        }

        return new FullDenseMatrix(newElements, numberOfColumns);
    }

    @Override
    public int getSize1() {
        return numberOfRows;
    }

    @Override
    public int getSize2() {
        return numberOfColumns;
    }

    @Override
    public RealMatrix multiply(double scalar) {
        double[] newElements = Arrays.copyOf(elements, elements.length);

        for (int i = 0; i < elements.length; i++) {
            newElements[i] *= scalar;
        }

        return new FullDenseMatrix(newElements, numberOfColumns);
    }

    @Override
    public MutableLinearSpaceObject<RealMatrix> toMutable() {
        // todo
        return null;
    }

    @Override
    public Iterator<RealMatrixElement> iterator() {
        if (elementList == null) {
            elementList = new ArrayList<>();
            int k = 0;
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    elementList.add(new RealMatrixElement(i, j, elements[k]));
                    k++;
                }
            }
        }

        return elementList.iterator();
    }
}
