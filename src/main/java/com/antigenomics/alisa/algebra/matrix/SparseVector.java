package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

import java.util.*;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.*;

/**
 * A one-dimensional vector backed by a sparse storage (list of indexed values).
 * One should work with this object as an iterable of IndexedVectorElement to achieve maximum performance.
 * Calling getAt method is slow as it requires iterating over the list of indexed values.
 * Sparse vector extends mutable linear object and can be scaled/added with vectors of the same length.
 * It also extends a vector space object and can be used in scalar and outer product with other vectors,
 * as well as linear, bilinear and symmetric bilinear forms with matrices.
 */
public class SparseVector
        extends Vector {
    /* Internal storage - typically a linked list */
    // todo: perhaps we need a custom List impl, although this one shows best performance in tests better than say GlueList, etc
    private final List<IndexedVectorValue> elementList;

    /**
     * Internal unsafe constructor, use the input list as is.
     *
     * @param elementList list of vector elements
     * @param length      true length of the vector
     */
    protected SparseVector(List<IndexedVectorValue> elementList,
                           int length) {
        this(elementList, length, false);
    }

    /**
     * Creates a sparse list from a list of indexed vector elements. If safe
     * is set to true, ensures that the list is sorted and doesn't contain duplicates;
     * and copies the list.
     *
     * @param elementList list of vector elements
     * @param length      true length of the vector
     * @param safe        if true, the list is copied and sorted if required.
     */
    public SparseVector(List<IndexedVectorValue> elementList,
                        int length, boolean safe) {
        super(length);
        if (safe) {
            this.elementList = checkAndSortIfNeeded(elementList, length);
        } else {
            this.elementList = elementList;
        }
    }

    /**
     * Creates a sparse vector from an array of double values.
     * Zero values are discarded.
     *
     * @param elements an array of vector values
     */
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


    @Override
    public DenseVector asDense() {
        return new DenseVector(elementList, length);
    }

    /* auxiliary methods, note that some methods are in LinearAlgebraUtils class */

    /**
     * Check the input list of elements, sort if needed and copy it.
     *
     * @param elementList input list
     * @param length      resulting vector length
     * @return checked input vector
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<IndexedVectorValue> checkAndSortIfNeeded(List<IndexedVectorValue> elementList,
                                                                int length) {
        int prevIndex = -1;
        boolean sorted = true;
        List<IndexedVectorValue> elementListCopy = new LinkedList<>();

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

            if (indexedVectorValue.getDoubleValue() != 0) {
                elementListCopy.add(indexedVectorValue);
            }
        }

        if (!sorted) {
            final Object[] arr = elementListCopy.toArray();
            elementListCopy = new LinkedList<>();
            Arrays.sort(arr, (Comparator) Comparator.naturalOrder());
            for (Object a : arr) {
                IndexedVectorValue v = (IndexedVectorValue) a;
                if (v.getDoubleValue() != 0) {
                    elementListCopy.add(v);
                }
            }
        }

        return elementListCopy;
    }

    /**
     * Copy the internal list of elements.
     *
     * @return new linked list holding the same elements as the same vector
     */
    private List<IndexedVectorValue> copyList() {
        List<IndexedVectorValue> storage = new LinkedList<>();
        for (IndexedVectorValue x : this) {
            storage.add(x);
        }
        return storage;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for (IndexedVectorValue element : elementList) {
            joiner.add(element.getIndex() + ": " + Float.toString((float) element.getDoubleValue()));
        }
        return "[" + joiner.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SparseVector that = (SparseVector) o;

        return elementList.equals(that.elementList);
    }

    @Override
    public int hashCode() {
        return elementList.hashCode();
    }
}
