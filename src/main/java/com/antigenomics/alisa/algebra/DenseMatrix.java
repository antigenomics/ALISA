package com.antigenomics.alisa.algebra;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.*;

public class DenseMatrix
        extends Matrix {
    private final double[] elements;
    private LinkedList<IndexedMatrixValue> elementList = null; // todo: faster to provide an iterator

    public DenseMatrix(double[] elements, int numberOfColumns) {
        super(computeNumberOfFullMatrixRows(elements.length, numberOfColumns), numberOfColumns);
        this.elements = elements;
    }

    public DenseMatrix(Iterable<IndexedMatrixValue> elements, int numberOfRows, int numberOfColumns) {
        super(numberOfRows, numberOfColumns);
        this.elements = new double[numberOfRows * numberOfColumns];
        for (IndexedMatrixValue e : elements) {
            this.elements[getFullMatrixIndex(e.getRowIndex(), e.getColIndex(), numberOfColumns)] = e.getDoubleValue();
        }
    }

    @Override
    public double getAt(int rowIndex, int columnIndex) {
        return elements[getFullMatrixIndex(rowIndex, columnIndex, numberOfColumns)];
    }

    @Override
    protected double getAt(int linearIndex) {
        return elements[linearIndex];
    }

    @Override
    public Matrix transpose() {
        double[] newElements = new double[elements.length];

        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                newElements[getFullMatrixIndex(j, i, numberOfRows)] = elements[k];
                k++;
            }
        }

        return new DenseMatrix(newElements, numberOfRows);
    }

    @Override
    protected final double bilinearFormUnchecked(Vector a) {
        if (a.isSparse()) {
            return bfSS2(a, a);
        } else {
            return bfDD2(a, a);
        }
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
            for (int j = 0; j < numberOfColumns; j++) {
                double bValue = b.getAt(j);
                for (int i = 0; i < numberOfRows; i++) {
                    resVector[i] += getAt(i, j) * bValue;
                }
            }
        }
        return new DenseVector(resVector);
    }

    @Override
    protected double bilinearFormUnchecked(Vector a, Vector b) {
        if (a.isSparse()) {
            if (b.isSparse()) {
                return bfSS(a, b);
            } else {
                return bfSD(a, b);
            }
        } else if (b.isSparse()) {
            return bfDS(a, b);
        } else {
            return bfDD(a, b);
        }
    }


    @Override
    protected Matrix addUnchecked(Matrix other) {
        double[] newElements = Arrays.copyOf(elements, elements.length);
        addImpl(Arrays.copyOf(newElements, elements.length), other);
        return new DenseMatrix(newElements, numberOfColumns);
    }

    @Override
    protected void addInplaceUnchecked(Matrix other) {
        addImpl(elements, other);
    }

    @Override
    public Matrix multiply(double scalar) {
        return new DenseMatrix(LinearAlgebraUtils.scale(elements, scalar),
                numberOfColumns);
    }

    @Override
    public void multiplyInplace(double scalar) {
        LinearAlgebraUtils.scaleInplace(elements, scalar);
    }

    @Override
    public Matrix deepCopy() {
        return new DenseMatrix(Arrays.copyOf(elements, elements.length),
                numberOfColumns);
    }

    private LinkedList<IndexedMatrixValue> getIndexedValues() {
        if (elementList == null) {
            elementList = new LinkedList<>();

            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    double value = getAt(i, j);

                    if (value != 0) {
                        elementList.add(new IndexedMatrixValue(i, j, value));
                    }
                }
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
        return numberOfRows * numberOfColumns;
    }

    @Override
    public Matrix asSparse() {
        return new SparseMatrix(getIndexedValues(), numberOfRows, numberOfColumns);
    }

    @Override
    public Matrix asDense() {
        return deepCopy();
    }

    /*   aux    */

    private double bfSS(Vector a, Vector b) {
        if (a.getEffectiveSize() > b.getEffectiveSize()) {
            // outer loop should be ran for the smallest of vectors
            return bfSS2(a, b);
        }

        double res = 0;
        for (IndexedVectorValue ai : a) {
            final double aValue = ai.getDoubleValue();
            final int i = ai.getIndex();
            for (IndexedVectorValue bj : b) {
                res += aValue * getAt(i, bj.getIndex()) * bj.getDoubleValue();
            }
        }
        return res;
    }

    private double bfSS2(Vector a, Vector b) {
        double res = 0;
        for (IndexedVectorValue bj : b) {
            final double bValue = bj.getDoubleValue();
            final int j = bj.getIndex();
            for (IndexedVectorValue ai : a) {
                res += ai.getDoubleValue() * getAt(ai.getIndex(), j) * bValue;
            }
        }
        return res;
    }

    private double bfSD(Vector a, Vector b) {
        double res = 0;
        for (IndexedVectorValue ai : a) {
            final double value = ai.getDoubleValue();
            final int i = ai.getIndex();
            for (int j = 0; j < b.getLength(); j++) {
                res += value * getAt(i, j) * b.getAt(j);
            }
        }
        return res;
    }

    private double bfDS(Vector a, Vector b) {
        double res = 0;
        for (IndexedVectorValue bj : b) {
            final double bValue = bj.getDoubleValue();
            final int j = bj.getIndex();
            for (int i = 0; i < a.getLength(); i++) {
                res += a.getAt(i) * getAt(i, j) * bValue;
            }
        }
        return res;
    }

    private double bfDD(Vector a, Vector b) {
        if (a.getEffectiveSize() > b.getEffectiveSize()) {
            // outer loop should be ran for the smallest of vectors
            return bfSS2(a, b);
        }

        double res = 0;
        for (int i = 0; i < a.getLength(); i++) {
            final double aValue = a.getAt(i);
            for (int j = 0; j < b.getLength(); j++) {
                res += aValue * getAt(i, j) * b.getAt(j);
            }
        }
        return res;
    }

    private double bfDD2(Vector a, Vector b) {
        double res = 0;
        for (int j = 0; j < b.getLength(); j++) {
            final double bValue = b.getAt(j);
            for (int i = 0; i < a.getLength(); i++) {
                res += a.getAt(i) * getAt(i, j) * bValue;
            }
        }
        return res;
    }

    private void addImpl(double[] elements, Matrix other) {
        if (other.isSparse()) {
            if (other.isLowerTriangular) {
                for (IndexedMatrixValue e : other) {
                    elements[getFullMatrixIndex(e.getRowIndex(), e.getColIndex(), numberOfColumns)] += e.getDoubleValue();
                    elements[getFullMatrixIndex(e.getColIndex(), e.getRowIndex(), numberOfColumns)] += e.getDoubleValue();
                }
            } else {
                for (IndexedMatrixValue e : other) {
                    elements[getFullMatrixIndex(e.getRowIndex(), e.getColIndex(), numberOfColumns)] += e.getDoubleValue();
                }
            }
        } else {
            if (other.isLowerTriangular) {
                int k = 0;
                for (int i = 0; i < numberOfRows; i++) {
                    for (int j = 0; j < numberOfColumns; j++) {
                        elements[k] += other.getAt(i, j);
                        k++;
                    }
                }
            } else {
                for (int i = 0; i < elements.length; i++) {
                    elements[i] += other.getAt(i);
                }
            }
        }
    }
}
