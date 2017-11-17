package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SparseMatrix
        extends Matrix {
    protected final List<IndexedMatrixValue> elementList;

    private static void check(Iterable<IndexedMatrixValue> elementList,
                              int numberOfRows, int numberOfColumns) {
        IndexedMatrixValue prevElement = IndexedMatrixValue.EMPTY;
        for (IndexedMatrixValue indexedMatrixValue : elementList) {
            int i = indexedMatrixValue.getRowIndex(),
                    j = indexedMatrixValue.getColIndex();

            if (i < 0 || i >= numberOfRows ||
                    j < 0 || j >= numberOfColumns) {
                throw new IndexOutOfBoundsException();
            }

            if (prevElement.compareTo(indexedMatrixValue) >= 0) {
                throw new IllegalArgumentException();
            }

            prevElement = indexedMatrixValue;
        }
    }

    protected SparseMatrix(List<IndexedMatrixValue> elementList,
                           int numberOfRows, int numberOfColumns) {
        this(elementList, numberOfRows, numberOfColumns, false);
    }

    // todo: only safe constructors public? specific static methods for unsafe
    public SparseMatrix(List<IndexedMatrixValue> elementList,
                        int numberOfRows, int numberOfColumns,
                        boolean safe) {
        super(numberOfRows, numberOfColumns);
        if (safe) {
            check(elementList, numberOfRows, numberOfColumns);
            this.elementList = new LinkedList<>(elementList);
        } else {
            this.elementList = elementList;
        }
    }

    @Override
    protected double bilinearFormUnchecked(Vector a, Vector b) {
        return a.dotProduct(linearFormUnchecked(b));
    }

    @Override
    protected double bilinearFormUnchecked(Vector a) {
        return a.dotProduct(linearFormUnchecked(a));
    }

    @Override
    protected Vector linearFormUnchecked(Vector b) {
        LinkedList<IndexedVectorValue> values = new LinkedList<>();

        double res = 0;

        if (numberOfRows > 0) {
            int previousIndex = elementList.iterator().next().getRowIndex();

            if (b.isSparse()) {
                Iterator<IndexedVectorValue> iterB = b.iterator();
                for (IndexedMatrixValue e : elementList) {
                    int currentIndex = e.getRowIndex();
                    if (currentIndex > previousIndex) {
                        values.add(new IndexedVectorValue(previousIndex, res));
                        previousIndex = currentIndex;
                        res = 0;
                        iterB = b.iterator();
                    } else {
                        IndexedVectorValue elemB = null;
                        while (iterB.hasNext() && (elemB = iterB.next()).getIndex() < e.getColIndex()) ;
                        if (elemB != null && elemB.getIndex() == e.getColIndex()) {
                            res += e.getDoubleValue() * elemB.getDoubleValue();
                        }
                    }
                }
            } else {
                for (IndexedMatrixValue e : elementList) {
                    int currentIndex = e.getRowIndex();
                    if (currentIndex > previousIndex) {
                        if (res != 0)
                            values.add(new IndexedVectorValue(previousIndex, res));
                        previousIndex = currentIndex;
                        res = 0;
                    } else {
                        res += e.getDoubleValue() * b.getAt(e.getColIndex());
                    }
                }
            }
        }

        return new SparseVector(values, numberOfRows);
    }

    @Override
    protected Matrix addUnchecked(Matrix other) {
        if (other.isSparse()) {
            LinkedList<IndexedMatrixValue> newElements = new LinkedList<>();
            LinearAlgebraUtils.combineAdd(newElements, this, other);
            return new SparseMatrix(newElements, numberOfRows, numberOfColumns);
        } else {
            return other.addUnchecked(this);
        }
    }

    @Override
    protected void addInplaceUnchecked(Matrix other) {
        List<IndexedMatrixValue> elementsCopy = copyList();
        elementList.clear();
        LinearAlgebraUtils.combineAdd(elementList, elementsCopy, other);
    }

    @Override
    public double getAt(int rowIndex, int columnIndex) {
        return elementList
                .stream()
                .filter(x -> x.getRowIndex() == rowIndex && x.getColIndex() == columnIndex)
                .findFirst()
                .orElse(IndexedMatrixValue.EMPTY)
                .getDoubleValue();
    }

    @Override
    protected double getAt(int linearIndex) {
        throw new NotImplementedException();
    }

    @Override
    public Matrix transpose() {
        return new SparseMatrix(elementList.
                stream()
                .map(x -> new IndexedMatrixValue(x.getColIndex(), x.getRowIndex(), x.getDoubleValue()))
                .collect(Collectors.toCollection(LinkedList::new)),
                numberOfColumns, numberOfRows);
    }

    @Override
    public boolean isSparse() {
        return true;
    }

    @Override
    public int getEffectiveSize() {
        return elementList.size();
    }

    @Override
    public Matrix asSparse() {
        return deepCopy();
    }

    @Override
    public Matrix asDense() {
        return new DenseMatrix(elementList, numberOfRows, numberOfColumns);
    }

    @Override
    public Matrix multiply(double scalar) {
        LinkedList<IndexedMatrixValue> newElements = new LinkedList<>();
        LinearAlgebraUtils.scale(newElements, elementList, scalar);

        return new SparseMatrix(newElements, numberOfRows, numberOfColumns);
    }

    @Override
    public void multiplyInplace(double scalar) {
        List<IndexedMatrixValue> copiedElements = copyList();
        elementList.clear();
        LinearAlgebraUtils.scale(elementList, copiedElements, scalar);
    }

    protected List<IndexedMatrixValue> copyList() {
        return new LinkedList<>(elementList);
    }

    @Override
    public Matrix deepCopy() {
        return new SparseMatrix(copyList(), numberOfRows, numberOfColumns);
    }

    @Override
    public Iterator<IndexedMatrixValue> iterator() {
        return elementList.iterator();
    }
}
