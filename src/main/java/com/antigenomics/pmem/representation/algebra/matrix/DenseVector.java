package com.antigenomics.pmem.representation.algebra.matrix;

import com.antigenomics.pmem.representation.LinearSpaceObjectUtils;
import com.antigenomics.pmem.representation.MutableLinearSpaceObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class DenseVector
        extends SafeRealVector {
    private final double[] elements;
    private List<RealVectorElement> elementList = null;

    public DenseVector(double[] elements) {
        this.elements = elements;
    }

    @Override
    public int getSize() {
        return elements.length;
    }

    @Override
    public double getAt(int i) {
        return elements[i];
    }

    @Override
    public double dotProductUnchecked(RealVector b) {
        double res = 0;

        if (b.isSparse()) {
            for (RealVectorElement e : b) {
                res += elements[e.getIndex()] * e.getValue();
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                res += elements[i] * b.getAt(i);
            }
        }


        return res;
    }

    @Override
    public RealMatrix outerProduct(RealVector b) {
        if (b == this) { // strictly symmetric
            final double[] matElements = new double[LowerTriangularDenseMatrix.LowerTriangularMatrixStorageIndex
                    .getLength(elements.length)];

            int k = 0;
            for (int i = 0; i < elements.length; i++) {
                for (int j = 0; j <= i; j++) {
                    matElements[k] = elements[i] * elements[j];
                    k++;
                }
            }

            return new LowerTriangularDenseMatrix(matElements);
        } else {
            int numberOfColumns = b.getSize();
            final double[] matElements = new double[elements.length * numberOfColumns];

            for (int i = 0; i < elements.length; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    matElements[i * numberOfColumns + j] = elements[i] * b.getAt(j);
                }
            }

            return new FullDenseMatrix(matElements, numberOfColumns);
        }
    }

    @Override
    public RealVector plusUnchecked(RealVector other) {
        final double[] newElements = Arrays.copyOf(elements, elements.length);

        if (other.isSparse()) {
            for (RealVectorElement e : other) {
                newElements[e.getIndex()] += e.getValue();
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                newElements[i] += other.getAt(i);
            }
        }

        return new DenseVector(newElements);
    }

    @Override
    public RealVector multiply(double scalar) {
        final double[] newElements = Arrays.copyOf(elements, elements.length);

        for (int i = 0; i < elements.length; i++) {
            newElements[i] *= scalar;
        }

        return new DenseVector(newElements);
    }

    @Override
    public MutableLinearSpaceObject<RealVector> toMutable() {
        // todo: vectors cannot be mutable - implement exception class
        throw new NotImplementedException();
    }

    @Override
    public double norm1() {
        return LinearSpaceObjectUtils.norm1(elements);
    }

    @Override
    public double norm2() {
        return LinearSpaceObjectUtils.norm2(elements);
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public int getEffectiveSize() {
        return elements.length;
    }

    @Override
    public Iterator<RealVectorElement> iterator() {
        // very slow
        if (elementList == null) {
            elementList = new ArrayList<>();

            for (int i = 0; i < elements.length; i++) {
                double value = elements[i];

                if (value != 0) {
                    elementList.add(new RealVectorElement(i, value));
                }
            }
        }

        return elementList.iterator();
    }
}
