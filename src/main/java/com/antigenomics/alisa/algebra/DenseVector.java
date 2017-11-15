package com.antigenomics.alisa.algebra;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DenseVector
        extends Vector {
    private final double[] elements;
    private LinkedList<IndexedVectorValue> elementList = null;

    public DenseVector(double[] elements) {
        super(elements.length);
        this.elements = elements;
    }

    public DenseVector(List<IndexedVectorValue> elementList, int length) {
        super(length);
        this.elements = new double[length];

        for (IndexedVectorValue indexedVectorValue : elementList) {
            elements[indexedVectorValue.getIndex()] = indexedVectorValue.getDoubleValue();
        }
    }

    private void addImpl(double[] elements, Vector other) {
        if (other.isSparse()) {
            for (IndexedVectorValue e : other) {
                elements[e.getIndex()] += e.getDoubleValue();
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                elements[i] += other.getAt(i);
            }
        }
    }

    @Override
    protected Vector addUnchecked(Vector other) {
        double[] newElements = Arrays.copyOf(elements, elements.length);
        addImpl(newElements, other);
        return new DenseVector(newElements);
    }

    @Override
    protected void addInplaceUnchecked(Vector other) {
        addImpl(elements, other);
    }

    @Override
    protected double dotProductUnchecked(Vector other) {
        double res = 0;

        if (other.isSparse()) {
            for (IndexedVectorValue e : other) {
                res += elements[e.getIndex()] * e.getDoubleValue();
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                res += elements[i] * other.getAt(i);
            }
        }

        return res;
    }

    @Override
    public Vector multiply(double scalar) {
        return new DenseVector(LinearAlgebraUtils.scale(elements, scalar));
    }

    @Override
    public void multiplyInplace(double scalar) {
        LinearAlgebraUtils.scaleInplace(elements, scalar);
    }

    @Override
    public Matrix outerProduct(Vector b) {
        if (this == b) {
            return expand();
        }

        int numberOfColumns = b.getLength();
        double[] matElements = new double[elements.length * numberOfColumns];

        int k = 0;
        for (double element : elements) {
            for (int j = 0; j < numberOfColumns; j++) {
                matElements[k] = element * b.getAt(j);
                k++;
            }
        }

        return new DenseMatrix(matElements, numberOfColumns);
    }

    @Override
    public Matrix expand() {
        double[] matElements = new double[LinearAlgebraUtils.getTriangularMatrixLength(elements.length)];

        int k = 0;
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j <= i; j++) {
                matElements[k] = elements[i] * elements[j];
                k++;
            }
        }

        return new DenseTriangularMatrix(matElements);
    }

    @Override
    public double getAt(int index) {
        return elements[index];
    }

    private LinkedList<IndexedVectorValue> getIndexedValues() {
        if (elementList == null) {
            elementList = new LinkedList<>();

            for (int i = 0; i < elements.length; i++) {
                double value = elements[i];

                if (value != 0) {
                    elementList.add(new IndexedVectorValue(i, value));
                }
            }
        }

        return elementList;
    }

    @Override
    public Iterator<IndexedVectorValue> iterator() {
        return getIndexedValues().iterator();
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public int getEffectiveSize() {
        return length;
    }

    @Override
    public Vector deepCopy() {
        return new DenseVector(Arrays.copyOf(elements, elements.length));
    }

    @Override
    public Vector asSparse() {
        return new SparseVector(getIndexedValues(), length);
    }

    @Override
    public Vector asDense() {
        return deepCopy();
    }
}
