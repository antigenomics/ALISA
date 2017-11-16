package com.antigenomics.alisa.algebra;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringJoiner;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.getTriangularMatrixIndex;

public class DenseTriangularMatrix extends Matrix {
    private final double[] elements;

    public DenseTriangularMatrix(double[] elements) {
        super(LinearAlgebraUtils.getTriangularMatrixSize(elements.length));
        this.elements = elements;
    }

    @Override
    public double getAt(int rowIndex, int columnIndex) {
        return elements[getTriangularMatrixIndex(rowIndex, columnIndex)];
    }

    @Override
    protected double getAt(int linearIndex) {
        return elements[linearIndex];
    }

    @Override
    public Matrix transpose() {
        return this;
    }

    @Override
    protected double bilinearFormUnchecked(Vector a, Vector b) {
        if (a == b) {
            return bilinearFormUnchecked(a);
        }
        throw new NotImplementedException();
    }

    @Override
    protected final double bilinearFormUnchecked(Vector a) {
        double res = 0;

        if (a.isSparse()) {
            for (IndexedVectorValue ei : a) {
                for (IndexedVectorValue ej : a) {
                    // todo: can optimize here?
                    if (ei.compareTo(ej) > -1) {
                        res += ei.getDoubleValue() * getAt(ei.getIndex(), ej.getIndex()) * ej.getDoubleValue();
                    }
                }
            }
        } else {
            for (int i = 0; i < a.getLength(); i++) {
                double value = a.getAt(i);
                for (int j = 0; j <= i; j++) {
                    res += value * getAt(i, j) * a.getAt(j);
                }
            }
        }

        return res;
    }

    @Override
    protected Vector linearFormUnchecked(Vector b) {
        double[] resVector = new double[numberOfRows];
        if (b.isSparse()) {
            for (IndexedVectorValue bj : b) {
                for (int i = 0; i < numberOfRows; i++) {
                    resVector[i] += getAt(i, bj.getIndex()) * bj.getDoubleValue();
                }
            }
        } else {
            // todo: can optimize?
            for (int j = 0; j < numberOfRows; j++) {
                double bValue = b.getAt(j);
                for (int i = 0; i < numberOfRows; i++) {
                    double value = getAt(i, j) * bValue;
                    resVector[i] += value;
                }
            }
        }
        return new DenseVector(resVector);
    }

    @Override
    protected Matrix addUnchecked(Matrix other) {
        if (!other.isLowerTriangular) {
            return other.addUnchecked(this);
        }
        double[] newElements = Arrays.copyOf(elements, elements.length);
        addImpl(Arrays.copyOf(newElements, elements.length), other);
        return new DenseMatrix(newElements, numberOfColumns);
    }

    @Override
    protected void addInplaceUnchecked(Matrix other) {
        if (!other.isLowerTriangular) {
            throw new IllegalArgumentException("Cannot add non-triangular matrix to triangular one inplace");
        }
        addImpl(elements, other);
    }

    @Override
    public Matrix multiply(double scalar) {
        return new DenseTriangularMatrix(LinearAlgebraUtils.scale(elements, scalar));
    }

    @Override
    public void multiplyInplace(double scalar) {
        LinearAlgebraUtils.scaleInplace(elements, scalar);
    }

    @Override
    public Matrix deepCopy() {
        return new DenseTriangularMatrix(Arrays.copyOf(elements, elements.length));
    }

    private LinkedList<IndexedMatrixValue> getIndexedValues() {
        LinkedList<IndexedMatrixValue> elementList = new LinkedList<>();

        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j <= i; j++) {
                double value = elements[k];

                if (value != 0) {
                    elementList.add(new IndexedMatrixValue(i, j, value));
                }

                k++;
            }
        }

        return elementList;
    }

    @Override
    public Iterator<IndexedMatrixValue> iterator() {
        return getIndexedValues().iterator();
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
    public Matrix asSparse() {
        // todo
        throw new NotImplementedException();
    }

    @Override
    public Matrix asDense() {
        return deepCopy();
    }

    private void addImpl(double[] elements, Matrix other) {
        if (other.isSparse()) {
            for (IndexedMatrixValue e : other) {
                int index = LinearAlgebraUtils.getTriangularMatrixIndex(e.getRowIndex(), e.getColIndex());
                elements[index] += e.getDoubleValue();
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                elements[i] += other.getAt(i);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < numberOfRows; i++) {
            StringJoiner joiner = new StringJoiner(", ");
            for (int j = 0; j <= i; j++) {
                joiner.add(Double.toString(getAt(i, j)));
            }
            for (int j = i + 1; j < numberOfRows; j++) {
                joiner.add("-");
            }
            res.append("[").append(joiner.toString()).append("]");
            if (i != numberOfRows - 1) {
                res.append("\n");
            }
        }

        return res.toString();
    }
}