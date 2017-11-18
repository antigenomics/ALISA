package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.*;

/**
 * A two-dimensional symmetric matrix backed by a dense (primitive) array storage. Only the lower triangle of
 * full square matrix is stored, i.e. only elements $x_{ij}$ with $i \leq j$.
 * Fast operations with this matrix involve calling getAt method and work for any i,j combination.
 * Working with this object as an iterable of IndexedMatrixElement is slow as the inner array is copied
 * to a list every time an iterator is called, moreover the iteration will run only across lower triangular
 * elements.
 * Dense symmetric matrix extends mutable linear object and can be scaled/added with matrices of the same row and column count.
 * It also extends a bilinear map object and can be used to map vectors to scalars via linear and
 * bilinear forms. Inplace addition only works if other matrix is also lower triangular.
 */
public class DenseTriangularMatrix extends Matrix {
    /* internal storage */
    private final double[] elements;

    /**
     * Internal unsafe constructor
     *
     * @param elements matrix values
     */
    protected DenseTriangularMatrix(final double[] elements) {
        this(elements, false);
    }

    /**
     * Creates a new dense matrix from a primitive array of elements.
     * The array is either copied or used as is depending on safe parameter.
     * Elements array stores matrix values in a linear way, i.e. first row elements up to diagonal one
     * (inclusive) are followed by the second row elements and so on.
     * Matrix size is computed from the length of provided array,
     * an exception is thrown if resulting matrix is not square.
     *
     * @param elements matrix values
     * @param safe     if true will use a deep copy of the array
     */
    public DenseTriangularMatrix(final double[] elements, final boolean safe) {
        super(getTriangularMatrixSize(elements.length));
        if (safe) {
            this.elements = Arrays.copyOf(elements, elements.length);
        } else {
            this.elements = elements;
        }
    }

    /**
     * Creates a new dense matrix from a list of indexed matrix values.
     * Elements array stores matrix values in a linear way, i.e. first row elements up to diagonal one
     * (inclusive) are followed by the second row elements and so on. All elements that lie above the diagonal
     * are discarded.
     * An exception is thrown if there are elements in the list that have row or column indices that are
     * out of bounds.
     *
     * @param elements     an interable of indexed matrix values
     * @param numberOfRows number of rows in resulting matrix
     */
    public DenseTriangularMatrix(Iterable<IndexedMatrixValue> elements, int numberOfRows) {
        super(numberOfRows);
        this.elements = new double[getTriangularMatrixLength(numberOfRows)];
        for (IndexedMatrixValue e : elements) {
            if (e.getRowIndex() <= e.getColIndex()) {
                int index = getTriangularMatrixIndex(e.getRowIndex(), e.getColIndex());
                this.elements[index] = e.getDoubleValue();
            }
        }
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
        // todo: discuss
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


    @Override
    public Iterator<IndexedMatrixValue> iterator() {
        return indexValues(new ArrayList<>()).iterator();
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
        return new SparseTriangularMatrix(indexValues(new LinkedList<>()), numberOfRows);
    }


    @Override
    public Matrix asDense() {
        return deepCopy();
    }

    /* Auxiliary methods */

    /**
     * Adds all values of this vector to a pre-initialized list.
     *
     * @param storage an empty list of indexed values
     * @return updated storage
     */
    private List<IndexedMatrixValue> indexValues(final List<IndexedMatrixValue> storage) {
        assert storage.isEmpty();

        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j <= i; j++) {
                double value = elements[k];

                if (value != 0) {
                    storage.add(new IndexedMatrixValue(i, j, value));
                }

                k++;
            }
        }

        return storage;
    }

    /**
     * Internal. Adds values from a given matrix to a pre-initialized storage array.
     * Supports only lower triangular matrices.
     *
     * @param elements pre-initialized array, to be modified in-place
     * @param other    matrix to add, should be lower triangular
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DenseTriangularMatrix that = (DenseTriangularMatrix) o;

        return Arrays.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }
}