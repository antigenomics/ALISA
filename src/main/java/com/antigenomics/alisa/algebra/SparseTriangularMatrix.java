package com.antigenomics.alisa.algebra;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;

public class SparseTriangularMatrix
        extends SparseMatrix {
    public SparseTriangularMatrix(LinkedList<IndexedMatrixValue> elementList,
                                  int numberOfRows) {
        super(elementList, numberOfRows, numberOfRows);
    }

    private SparseTriangularMatrix(SparseMatrix matrix) {
        super(matrix.elementList, matrix.numberOfRows, matrix.numberOfRows);
    }

    @Override
    public double getAt(int rowIndex, int columnIndex) {
        if (rowIndex > columnIndex) {
            return super.getAt(columnIndex, rowIndex);
        }
        return super.getAt(rowIndex, columnIndex);
    }

    @Override
    protected double bilinearFormUnchecked(Vector a, Vector b) {
        if (a == b) {
            return bilinearFormUnchecked(a);
        }
        throw new NotImplementedException();
    }

    @Override
    protected Matrix addUnchecked(Matrix other) {
        if (other.isLowerTriangular && other.isSparse()) {
            LinkedList<IndexedMatrixValue> newElements = new LinkedList<>();
            LinearAlgebraUtils.combineAdd(newElements, this, other);
            return new SparseTriangularMatrix(newElements, numberOfRows);
        } else {
            return super.addUnchecked(this);
        }
    }

    @Override
    protected void addInplaceUnchecked(Matrix other) {
        if (!other.isLowerTriangular) {
            throw new IllegalArgumentException("Cannot add non-triangular matrix to triangular one inplace");
        }

        super.addInplaceUnchecked(other);
    }

    @Override
    public Matrix deepCopy() {
        return new SparseTriangularMatrix(copyList(), numberOfRows);
    }

    @Override
    public Matrix multiply(double scalar) {
        LinkedList<IndexedMatrixValue> newElements = new LinkedList<>();
        LinearAlgebraUtils.scale(newElements, elementList, scalar);

        return new SparseTriangularMatrix(newElements, numberOfRows);
    }

    @Override
    public Matrix transpose() {
        return this;
    }

    @Override
    public double norm1() {
        double norm1 = 0,
                norm1D = 0;

        if (isSparse()) {
            for (IndexedMatrixValue x : this) {
                double value = Math.abs(x.getDoubleValue());
                norm1 += value;
                if (x.getRowIndex() == x.getColIndex()) {
                    norm1D += value;
                }
            }
        }

        return 2 * norm1 - norm1D;
    }

    @Override
    public double norm2() {
        double norm2 = 0,
                norm2D = 0;

        if (isSparse()) {
            for (IndexedMatrixValue x : this) {
                double value = x.getDoubleValue() * x.getDoubleValue();
                norm2 += value;
                if (x.getRowIndex() == x.getColIndex()) {
                    norm2D += value;
                }
            }
        }

        return Math.sqrt(2 * norm2 - norm2D);
    }
}
