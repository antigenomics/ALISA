package com.antigenomics.alisa.algebra;

public abstract class Matrix
        implements LinearSpaceObject<Matrix>, BilinearMap<Vector, Matrix>,
        Container<IndexedMatrixValue, Matrix> {
    protected final int numberOfRows, numberOfColumns;
    protected final boolean isLowerTriangular;

    public Matrix(int numberOfRows, int numberOfColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.isLowerTriangular = false;
    }

    public Matrix(int size) {
        this.numberOfRows = size;
        this.numberOfColumns = size;
        this.isLowerTriangular = true;
    }

    protected abstract double bilinearFormUnchecked(Vector a, Vector b);

    protected abstract double bilinearFormUnchecked(Vector a);

    protected abstract Vector linearFormUnchecked(Vector b);

    protected abstract Matrix addUnchecked(Matrix other);

    protected abstract void addInplaceUnchecked(Matrix other);

    public int getNumberOfRows() {
        return numberOfRows;
    }

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
    public Vector linearForm(Vector b) {
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

    private void checkSizeMatch(Matrix other) {
        if (numberOfRows != other.numberOfRows ||
                numberOfColumns != other.numberOfColumns)
            throw new IllegalArgumentException("Dimensions of matrices don't match");
    }

    private void checkSizeMatchSymmetric(Vector other) {
        if (!isLowerTriangular || numberOfRows != other.length)
            throw new IllegalArgumentException("Matrix is non-symmetric or vector length and number of rows don't match");
    }

    private void checkSizeMatchLeft(Vector other) {
        if (numberOfRows != other.length)
            throw new IllegalArgumentException("Vector length and number of rows don't match");
    }

    private void checkSizeMatchRight(Vector other) {
        if (numberOfColumns != other.length)
            throw new IllegalArgumentException("Vector length and number of columns don't match");
    }

    public boolean isLowerTriangular() {
        return isLowerTriangular;
    }

    public abstract double getAt(int rowIndex, int columnIndex);

    protected abstract double getAt(int linearIndex);

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
}
