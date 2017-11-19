package com.antigenomics.alisa.algebra.matrix;

import com.antigenomics.alisa.algebra.VectorMapping;
import com.antigenomics.alisa.algebra.Container;
import com.antigenomics.alisa.algebra.LinearSpaceObject;

import java.util.LinkedList;
import java.util.List;

/**
 * A generic two-dimensional real vector. The storage type (dense/sparse) depends on implementation.
 * Based on the storage type, fast access is implemented either via getAt (dense) or iterator (sparse) functions.
 * This class supports all linear algebra operations and can compute linear and bilinear forms
 * if supplied with Vector objects. This class implements basic checks to guarantee that
 * the dimensions of two matrices are matched for operations where it is required.
 * Linear algebra methods tagged as in-place are not guaranteed to be thread safe.
 */
public abstract class Matrix
        implements LinearSpaceObject<Matrix>, VectorMapping<Vector, Matrix>,
        Container<IndexedMatrixValue, Matrix> {
    /* true matrix shape */
    protected final int numberOfRows, numberOfColumns;
    protected final boolean isLowerTriangular;

    /* auxiliary constructors */

    /**
     * Create a dense matrix from an array of elements.
     *
     * @param values matrix elements
     * @return a dense matrix
     */
    public static DenseMatrix DENSE(double[][] values) {
        int numberOfRows = values.length,
                numberOfColumns = values[0].length;
        double[] arr = new double[numberOfRows * numberOfColumns];
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                arr[i * numberOfColumns + j] = values[i][j];
            }
        }
        return new DenseMatrix(arr, numberOfColumns);
    }

    /**
     * Create a sparse matrix from an array of elements.
     *
     * @param values matrix elements
     * @return a dense matrix
     */
    public static SparseMatrix SPARSE(double[][] values) {
        int numberOfRows = values.length,
                numberOfColumns = values[0].length;
        List<IndexedMatrixValue> elements = new LinkedList<>();
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                double value = values[i][j];

                if (value != 0) {
                    elements.add(new IndexedMatrixValue(i, j, value));
                }
            }
        }
        return new SparseMatrix(elements, numberOfRows, numberOfColumns);
    }

    /**
     * Creates a dense matrix of zeros
     *
     * @param numberOfRows    number of rows
     * @param numberOfColumns number of columns
     * @return a dense matrix
     */
    public static DenseMatrix DENSE_ZEROS(int numberOfRows, int numberOfColumns) {
        return new DenseMatrix(new double[numberOfRows * numberOfColumns],
                numberOfColumns);
    }

    /**
     * Creates an empty sparse matrix
     *
     * @param numberOfRows    number of rows
     * @param numberOfColumns number of columns
     * @return a sparse matrix
     */
    public static SparseMatrix SPARSE_ZEROS(int numberOfRows, int numberOfColumns) {
        return new SparseMatrix(new LinkedList<>(), numberOfRows,
                numberOfColumns);
    }


    /**
     * Creates a dense identity matrix with ones on the diagonal.
     * The resulting matrix is symmetric.
     *
     * @param size number of rows/columns
     * @return a dense matrix
     */
    public static DenseTriangularMatrix DENSE_EYE(int size) {
        double[] arr = new double[size * (size + 1) / 2];
        for (int i = 0; i < size; i++) {
            arr[i * (i + 3) / 2] = 1;
        }
        return new DenseTriangularMatrix(arr);
    }

    /**
     * Creates a sparse identity matrix with ones on the diagonal.
     * The resulting matrix is symmetric.
     *
     * @param size number of rows/columns
     * @return a sparse matrix
     */
    public static SparseTriangularMatrix SPARSE_EYE(int size) {
        List<IndexedMatrixValue> elements = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            elements.add(new IndexedMatrixValue(i, i, 1));
        }
        return new SparseTriangularMatrix(elements, size);
    }

    protected Matrix(int numberOfRows, int numberOfColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.isLowerTriangular = false;
    }

    protected Matrix(int size) {
        this.numberOfRows = size;
        this.numberOfColumns = size;
        this.isLowerTriangular = true;
    }

    protected abstract double bilinearFormUnchecked(Vector a, Vector b);

    protected abstract double bilinearFormUnchecked(Vector a);

    protected abstract Vector linearFormUnchecked(Vector b);

    protected abstract Matrix addUnchecked(Matrix other);

    protected abstract void addInplaceUnchecked(Matrix other);

    /**
     * Gets the number of rows in this matrix.
     *
     * @return number of rows
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Gets the number of columns in this matrix.
     *
     * @return number of columns
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }


    @Override
    public double bilinearForm(Vector a, Vector b) {
        checkSizeMatchLeft(a);
        checkSizeMatchRight(b);
        return bilinearFormUnchecked(a, b);
    }


    @Override
    public Vector map(Vector b) {
        checkSizeMatchRight(b);
        return linearFormUnchecked(b);
    }


    @Override
    public double bilinearForm(Vector a) {
        checkSizeMatchSymmetric(a);
        return bilinearFormUnchecked(a);
    }


    @Override
    public Matrix add(Matrix other) {
        checkSizeMatch(other);
        return addUnchecked(other);
    }


    @Override
    public void addInplace(Matrix other) {
        checkSizeMatch(other);
        addInplaceUnchecked(other);
    }

    /**
     * Internal check for object shape match.
     *
     * @param other other matrix
     */
    private void checkSizeMatch(Matrix other) {
        if (numberOfRows != other.numberOfRows ||
                numberOfColumns != other.numberOfColumns)
            throw new IllegalArgumentException("Dimensions of matrices don't match");
    }

    /**
     * Internal check for object shape match.
     *
     * @param other other vector
     */
    private void checkSizeMatchSymmetric(Vector other) {
        if (!isLowerTriangular || numberOfRows != other.length)
            throw new IllegalArgumentException("Matrix is non-symmetric or vector length and number of rows don't match");
    }

    /**
     * Internal check for object shape match.
     *
     * @param other other vector
     */
    private void checkSizeMatchLeft(Vector other) {
        if (numberOfRows != other.length)
            throw new IllegalArgumentException("Vector length and number of rows don't match");
    }

    /**
     * Internal check for object shape match.
     *
     * @param other other vector
     */
    private void checkSizeMatchRight(Vector other) {
        if (numberOfColumns != other.length)
            throw new IllegalArgumentException("Vector length and number of columns don't match");
    }

    /**
     * Checks if this matrix is stored as a lower triangular of a symmetric matrix.
     *
     * @return true if matrix is lower triangular.
     */
    public boolean isLowerTriangular() {
        return isLowerTriangular;
    }

    public abstract double getAt(int rowIndex, int columnIndex);

    /**
     * Internal. Gets the value at a given liner index in the internal storage.
     *
     * @param linearIndex linear index.
     * @return matrix value
     */
    protected abstract double getAt(int linearIndex);

    /**
     * Transpose this matrix, swapping row and column indices.
     * Does nothing for a symmetric lower triangular matrix.
     *
     * @return transposed matrix
     */
    public abstract Matrix transpose();


    @Override
    public double getAt(int... indices) {
        if (indices.length == 2) {
            return getAt(indices[0], indices[1]);
        }
        throw new IllegalArgumentException();
    }


    @Override
    public double norm1() {
        double norm1 = 0;

        if (isSparse()) {
            for (IndexedMatrixValue x : this) {
                norm1 += Math.abs(x.getDoubleValue());
            }
        } else {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    norm1 += Math.abs(getAt(i, j));
                }
            }
        }

        return norm1;
    }


    @Override
    public double norm2() {
        double norm2 = 0;

        if (isSparse()) {
            for (IndexedMatrixValue x : this) {
                norm2 += x.getDoubleValue() * x.getDoubleValue();
            }
        } else {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    double value = getAt(i, j);
                    norm2 += value * value;
                }
            }
        }

        return Math.sqrt(norm2);
    }


    @Override
    public double normInf() {
        double normInf = 0;

        if (isSparse()) {
            for (IndexedMatrixValue x : this) {
                normInf = Math.max(normInf, Math.abs(x.getDoubleValue()));
            }
        } else {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    normInf = Math.max(normInf, Math.abs(getAt(i, j)));
                }
            }
        }

        return normInf;
    }
}
