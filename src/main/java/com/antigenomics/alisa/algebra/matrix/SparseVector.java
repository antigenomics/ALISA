package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

import java.util.*;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.*;

public class SparseVector
        extends Vector {
    /* Internal storage - typically a linked list */
    private final List<IndexedVectorValue> elementList;

    protected SparseVector(List<IndexedVectorValue> elementList,
                           int length) {
        this(elementList, length, false);
    }

    public SparseVector(List<IndexedVectorValue> elementList,
                        int length, boolean safe) {
        super(length);
        if (safe) {
            this.elementList = checkAndSortIfNeeded(elementList, length);
        } else {
            this.elementList = elementList;
        }
    }

    public SparseVector(double[] elements) {
        super(elements.length);
        this.elementList = new LinkedList<>();
        for (int i = 0; i < elements.length; i++) {
            double value = elements[i];
            if (value != 0) {
                elementList.add(new IndexedVectorValue(i, value));
            }
        }
    }

    @Override
    protected Vector addUnchecked(Vector other) {
        if (other.isSparse()) {
            List<IndexedVectorValue> elements = new LinkedList<>();
            combineAdd(elements, this, other);
            return new SparseVector(elements, length);
        } else {
            return other.addUnchecked(this);
        }
    }

    @Override
    protected void addInplaceUnchecked(Vector other) {
        List<IndexedVectorValue> copy = copyList();
        elementList.clear();
        combineAdd(elementList, copy, other);
    }

    @Override
    protected double dotProductUnchecked(Vector other) {
        return elementList
                .stream()
                .mapToDouble(x -> x.getDoubleValue() * other.getAt(x.getIndex()))
                .sum();
    }

    @Override
    public double getAt(int index) {
        return elementList
                .stream()
                .filter(x -> x.getIndex() == index)
                .findFirst()
                .orElse(IndexedVectorValue.EMPTY)
                .getDoubleValue();
    }

    @Override
    public Vector multiply(double scalar) {
        LinkedList<IndexedVectorValue> newElements = new LinkedList<>();
        LinearAlgebraUtils.scale(newElements, elementList, scalar);

        return new SparseVector(newElements, length);
    }

    @Override
    public void multiplyInplace(double scalar) {
        List<IndexedVectorValue> copiedElements = copyList();
        elementList.clear();
        LinearAlgebraUtils.scale(elementList, copiedElements, scalar);
    }

    @Override
    public Matrix outerProduct(Vector b) {
        if (this == b) {
            return expand();
        }

        if (b.isSparse()) {
            LinkedList<IndexedMatrixValue> matrixValues = new LinkedList<>();
            for (IndexedVectorValue e1 : this) {
                for (IndexedVectorValue e2 : b) {
                    matrixValues.add(new IndexedMatrixValue(e1.getIndex(), e2.getIndex(),
                            e1.getDoubleValue() * e2.getDoubleValue()));
                }
            }
            return new SparseMatrix(matrixValues, this.length, b.length);
        } else {
            return b.outerProduct(this).transpose();
        }
    }

    @Override
    public Matrix expand() {
        // todo: can optimize here
        LinkedList<IndexedMatrixValue> matrixValues = new LinkedList<>();
        for (IndexedVectorValue e1 : this) {
            for (IndexedVectorValue e2 : this) {
                int i = e1.getIndex(), j = e2.getIndex();
                if (i <= j) {
                    matrixValues.add(new IndexedMatrixValue(i, j,
                            e1.getDoubleValue() * e2.getDoubleValue()));
                }
            }
        }
        return new SparseTriangularMatrix(matrixValues, this.length);
    }

    private List<IndexedVectorValue> copyList() {
        List<IndexedVectorValue> storage = new LinkedList<>();
        for (IndexedVectorValue x : this) {
            storage.add(x);
        }
        return storage;
    }

    @Override
    public Iterator<IndexedVectorValue> iterator() {
        return elementList.iterator();
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
    public Vector deepCopy() {
        return new SparseVector(copyList(), length);
    }

    @Override
    public Vector asSparse() {
        return deepCopy();
    }

    public DenseVector asDense() {
        return new DenseVector(elementList, length);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<IndexedVectorValue> checkAndSortIfNeeded(List<IndexedVectorValue> elementList,
                                                                int length) {
        int prevIndex = -1;
        boolean sorted = true;
        for (IndexedVectorValue indexedVectorValue : elementList) {
            int index = indexedVectorValue.getIndex();

            if (index < 0 || index >= length) {
                throw new IndexOutOfBoundsException();
            }

            if (prevIndex >= index) {
                if (prevIndex == index) {
                    throw new IllegalArgumentException("Should not contain duplicates");
                } else {
                    sorted = false;
                    break;
                }
            }

            prevIndex = index;
        }

        List<IndexedVectorValue> elementListCopy;

        if (!sorted) {
            elementListCopy = new LinkedList<>();
            final Object[] arr = elementList.toArray();
            Arrays.sort(arr, (Comparator) Comparator.naturalOrder());
            for (Object a : arr) {
                IndexedVectorValue v = (IndexedVectorValue) a;
                if (v.getDoubleValue() != 0) {
                    elementListCopy.add(v);
                }
            }
        } else {
            elementListCopy = new LinkedList<>(elementList);
        }

        return elementListCopy;
    }
}
