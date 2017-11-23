package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A two-dimensional matrix backed by a sparse (linked list) storage..
 * Fast operations with this matrix involve calling getAt method. Working with this object as an
 * iterable of IndexedMatrixElement is slow as the inner array is copied to a list every time an iterator is called.
 * Dense matrix extends mutable linear object and can be scaled/added with matrices of the same row and column count.
 * It also extends a bilinear map object and can be used to map vectors to scalars via linear and
 * bilinear forms.
 */
public abstract class SparseMatrix
        extends Matrix {
    protected final List<IndexedMatrixValue> elementList;

    /**
     * Create a sparse matrix with a specified number of rows and columns from a list of indexed values.
     * If safe is set to true, values will be checked for ordering, sorted if needed and copied, the list of elements
     * will be used as is otherwise. During the check, an exception will be thrown if there are duplicate elements or
     * if element indices are out of bounds.
     * Note that this constructor should never be used for lower triangular matrices, i.e. passing only
     * lower triangular elements to this constructor and setting number of rows equal to the number of columns
     * will not yield a symmetric matrix, but rather a matrix with all upper triangular elements being zeros.
     *
     * @param elementList     matrix values
     * @param numberOfRows    number of rows
     * @param numberOfColumns number of columns
     * @param safe            if set to true, will check ordering and copy the values list
     * @param lowerTriangular true if matrix is lower triangular
     */
    protected SparseMatrix(List<IndexedMatrixValue> elementList,
                           int numberOfRows, int numberOfColumns,
                           boolean safe, boolean lowerTriangular) {
        super(numberOfRows, numberOfColumns, true, lowerTriangular);
        if (safe) {
            this.elementList = checkSortCopy(elementList, numberOfRows, numberOfColumns);
        } else {
            this.elementList = elementList;
        }
    }

    /**
     * Create a new matrix of the same type (full/lower triangular)
     * from an array of elements
     *
     * @param elementList an array of matrix values
     * @return a matrix
     */
    protected abstract Matrix withElements(List<IndexedMatrixValue> elementList);

    @Override
    protected double bilinearFormUncheckedDD(Vector a, Vector b) {
        return a.dotProduct(linearFormUncheckedD(b));
    }

    @Override
    protected double bilinearFormUncheckedDS(Vector a, Vector b) {
        return bilinearFormUncheckedSS(a, b);
    }

    @Override
    protected double bilinearFormUncheckedSD(Vector a, Vector b) {
        return bilinearFormUncheckedDD(a, b);
    }

    @Override
    protected double bilinearFormUncheckedSS(Vector a, Vector b) {
        return a.dotProduct(linearFormUncheckedS(b));
    }

    @Override
    protected double bilinearFormUncheckedD(Vector a) {
        return bilinearFormUncheckedDD(a, a);
    }

    @Override
    protected double bilinearFormUncheckedS(Vector a) {
        return bilinearFormUncheckedSS(a, a);
    }

    @Override
    protected Vector linearFormUncheckedD(Vector b) {
        LinkedList<IndexedVectorValue> values = new LinkedList<>();

        double res = 0;

        if (numberOfRows > 0) {
            int previousIndex = elementList.iterator().next().getRowIndex();

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

        return new SparseVector(values, numberOfRows);
    }

    @Override
    protected Vector linearFormUncheckedS(Vector b) {
        LinkedList<IndexedVectorValue> values = new LinkedList<>();

        double res = 0;

        if (numberOfRows > 0) {
            int previousIndex = elementList.iterator().next().getRowIndex();

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
        }

        return new SparseVector(values, numberOfRows);
    }

    @Override
    protected Matrix addUncheckedD(Matrix other) {
        return other.addUncheckedS(this);
    }

    @Override
    protected Matrix addUncheckedS(Matrix other) {
        LinkedList<IndexedMatrixValue> newElements = new LinkedList<>();
        LinearAlgebraUtils.combineAdd(newElements, this, other);
        return withElements(newElements);
    }

    @Override
    protected void addInplaceUncheckedD(Matrix other) {
        addInplaceUncheckedS(other);
    }

    @Override
    protected void addInplaceUncheckedS(Matrix other) {
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
        return withElements(elementList.
                stream()
                .map(x -> new IndexedMatrixValue(x.getColIndex(), x.getRowIndex(), x.getDoubleValue()))
                .collect(Collectors.toCollection(LinkedList::new)));
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
        return isLowerTriangular ? new DenseTriangularMatrix(elementList, numberOfRows)
                : new DenseFullMatrix(elementList, numberOfRows, numberOfColumns);
    }

    @Override
    public Matrix multiply(double scalar) {
        LinkedList<IndexedMatrixValue> newElements = new LinkedList<>();
        LinearAlgebraUtils.scale(newElements, elementList, scalar);
        return withElements(newElements);
    }

    @Override
    public void multiplyInplace(double scalar) {
        List<IndexedMatrixValue> copiedElements = copyList();
        elementList.clear();
        LinearAlgebraUtils.scale(elementList, copiedElements, scalar);
    }

    @Override
    public Matrix deepCopy() {
        return withElements(copyList());
    }

    @Override
    public Iterator<IndexedMatrixValue> iterator() {
        return elementList.iterator();
    }

    /* auxiliary methods, note that some methods are in LinearAlgebraUtils class */

    /**
     * Returns a copy of current element list. As elements are immutable,
     * only shallow copy is required.
     *
     * @return shallow copy of element list
     */
    protected List<IndexedMatrixValue> copyList() {
        return new LinkedList<>(elementList);
    }

    /**
     * Check the input list of elements, sort if needed and copy it.
     *
     * @param elementList     input list
     * @param numberOfRows    number of rows in resulting matrix
     * @param numberOfColumns number of columns in resulting matrix
     * @return checked input matrix linear storage
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static List<IndexedMatrixValue> checkSortCopy(List<IndexedMatrixValue> elementList,
                                                            int numberOfRows, int numberOfColumns) {
        int prevRowIndex = -1, prevColIndex = -1;
        boolean sorted = true;
        List<IndexedMatrixValue> elementListCopy = new LinkedList<>();

        for (IndexedMatrixValue indexedMatrixValue : elementList) {
            int rowIndex = indexedMatrixValue.getRowIndex(),
                    colIndex = indexedMatrixValue.getColIndex();

            if (rowIndex < 0 || rowIndex >= numberOfRows ||
                    colIndex < 0 || colIndex >= numberOfColumns) {
                throw new IndexOutOfBoundsException();
            }

            if (prevRowIndex >= rowIndex || prevColIndex >= colIndex) {
                if (prevRowIndex == rowIndex || prevColIndex == colIndex) {
                    throw new IllegalArgumentException("Should not contain duplicates");
                } else {
                    sorted = false;
                    break;
                }
            }

            prevRowIndex = rowIndex;
            prevColIndex = colIndex;

            if (indexedMatrixValue.getDoubleValue() != 0) {
                elementListCopy.add(indexedMatrixValue);
            }
        }

        if (!sorted) {
            final Object[] arr = elementListCopy.toArray();
            elementListCopy = new LinkedList<>();
            Arrays.sort(arr, (Comparator) Comparator.naturalOrder());
            for (Object a : arr) {
                IndexedMatrixValue v = (IndexedMatrixValue) a;
                if (v.getDoubleValue() != 0) {
                    elementListCopy.add(v);
                }
            }
        }

        return elementListCopy;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for (IndexedMatrixValue element : elementList) {
            joiner.add("(" + element.getRowIndex() + "," + element.getColIndex() + "): " +
                    Float.toString((float) element.getDoubleValue()));
        }
        return "[" + joiner.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SparseMatrix that = (SparseMatrix) o;

        return elementList.equals(that.elementList);
    }

    @Override
    public int hashCode() {
        return elementList.hashCode();
    }

    @Override
    public double norm1() {
        double norm1 = 0;

        for (IndexedMatrixValue x : this) {
            norm1 += Math.abs(x.getDoubleValue());
        }

        return norm1;
    }

    @Override
    public double norm2() {
        double norm2 = 0;

        for (IndexedMatrixValue x : this) {
            norm2 += x.getDoubleValue() * x.getDoubleValue();
        }

        return Math.sqrt(norm2);
    }

    @Override
    public double normInf() {
        double normInf = 0;

        for (IndexedMatrixValue x : this) {
            normInf = Math.max(normInf, Math.abs(x.getDoubleValue()));
        }

        return normInf;
    }
}
