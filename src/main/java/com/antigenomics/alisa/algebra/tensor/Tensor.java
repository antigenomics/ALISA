package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.Container;
import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.algebra.VectorMapping;

public abstract class Tensor
        implements LinearSpaceObject<Tensor>,
        VectorMapping<CategoricalVector, Tensor>, Container<IndexedTensorValue, Tensor> {
    /* true tensor shape */
    protected final int numberOfRows, numberOfColumns,
            numberOfCategoryRows, numberOfCategoryColumns;
    protected final boolean isSymmetric, isSemiSymmetric;

    protected Tensor(int numberOfRows, int numberOfColumns,
                     int numberOfCategoryRows, int numberOfCategoryColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfCategoryRows = numberOfCategoryRows;
        this.numberOfCategoryColumns = numberOfCategoryColumns;
        this.isSymmetric = false;
        this.isSemiSymmetric = false;
    }

    protected Tensor(int numberOfRows, int numberOfColumns,
                     int numberCategories) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfCategoryRows = numberCategories;
        this.numberOfCategoryColumns = numberCategories;
        this.isSymmetric = false;
        this.isSemiSymmetric = true;
    }

    protected Tensor(int size,
                     int numberCategories) {
        this.numberOfRows = size;
        this.numberOfColumns = size;
        this.numberOfCategoryRows = numberCategories;
        this.numberOfCategoryColumns = numberCategories;
        this.isSymmetric = true;
        this.isSemiSymmetric = false;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int getNumberRowCategories() {
        return numberOfCategoryRows;
    }

    public int getNumberOfCategoryColumns() {
        return numberOfCategoryColumns;
    }

    public boolean isSymmetric() {
        return isSymmetric;
    }

    public boolean isSemiSymmetric() {
        return isSemiSymmetric;
    }

    protected abstract double bilinearFormUnchecked(CategoricalVector a, CategoricalVector b);

    protected double bilinearFormUnchecked(CategoricalVector a) {
        return bilinearFormUnchecked(a, a);
    }

    protected abstract CategoricalVector linearFormUnchecked(CategoricalVector b);

    protected abstract Tensor addUnchecked(Tensor other);

    protected abstract void addInplaceUnchecked(Tensor other);

    @Override
    public double bilinearForm(CategoricalVector a, CategoricalVector b) {
        checkSizeMatchLeft(a);
        checkSizeMatchRight(b);
        return bilinearFormUnchecked(a, b);
    }


    @Override
    public CategoricalVector map(CategoricalVector b) {
        checkSizeMatchRight(b);
        return linearFormUnchecked(b);
    }


    @Override
    public double bilinearForm(CategoricalVector a) {
        checkSizeMatchSymmetric(a);
        return bilinearFormUnchecked(a);
    }


    @Override
    public Tensor add(Tensor other) {
        checkSizeMatch(other);
        return addUnchecked(other);
    }


    @Override
    public void addInplace(Tensor other) {
        checkSizeMatch(other);
        addInplaceUnchecked(other);
    }

    public abstract double getAt(int rowIndex, int columnIndex,
                                 int rowCategory, int columnCategory);

    /**
     * Internal. Gets the value at a given liner index in the internal storage.
     *
     * @param linearIndex linear index.
     * @return matrix value
     */
    protected abstract double getAt(int linearIndex);

    @Override
    public double getAt(int... indices) {
        if (indices.length == 4) {
            return getAt(indices[0], indices[1], indices[2], indices[3]);
        }
        throw new IllegalArgumentException();
    }

    /**
     * Internal check for object shape match.
     *
     * @param other other tensor
     */
    private void checkSizeMatch(Tensor other) {
        if (numberOfRows != other.numberOfRows ||
                numberOfColumns != other.numberOfColumns ||
                numberOfCategoryRows != other.numberOfCategoryRows ||
                numberOfCategoryColumns != other.numberOfCategoryColumns)
            throw new IllegalArgumentException("Dimensions of tensors don't match");
    }

    /**
     * Internal check for object shape match.
     *
     * @param other a categorical vector
     */
    private void checkSizeMatchSymmetric(CategoricalVector other) {
        if (!isSymmetric ||
                numberOfRows != other.getLength() ||
                numberOfCategoryRows != other.getNumberOfCategories())
            throw new IllegalArgumentException("Tensor is non-symmetric or don't mach categorical vector " +
                    "length/number of categories");
    }

    /**
     * Internal check for object shape match.
     *
     * @param other a categorical vector
     */
    private void checkSizeMatchLeft(CategoricalVector other) {
        if (numberOfRows != other.getLength() ||
                numberOfCategoryRows != other.getNumberOfCategories())
            throw new IllegalArgumentException("Tensor number of rows or number of category rows don't mach " +
                    "categorical vector length/number of categories");
    }

    /**
     * Internal check for object shape match.
     *
     * @param other a categorical vector
     */
    private void checkSizeMatchRight(CategoricalVector other) {
        if (numberOfColumns != other.getLength() ||
                numberOfCategoryColumns != other.getNumberOfCategories())
            throw new IllegalArgumentException("Tensor number of columns or number of category columns don't mach " +
                    "categorical vector length/number of categories");
    }

    @Override
    public double norm1() {
        double norm1 = 0;

        if (isSparse()) {
            for (IndexedTensorValue x : this) {
                norm1 += Math.abs(x.getDoubleValue());
            }
        } else {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    for (int a = 0; a < numberOfCategoryRows; a++) {
                        for (int b = 0; b < numberOfCategoryColumns; b++) {
                            norm1 += Math.abs(getAt(i, j, a, b));
                        }
                    }
                }
            }
        }

        return norm1;
    }


    @Override
    public double norm2() {
        double norm2 = 0;

        if (isSparse()) {
            for (IndexedTensorValue x : this) {
                norm2 += x.getDoubleValue() * x.getDoubleValue();
            }
        } else {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    for (int a = 0; a < numberOfCategoryRows; a++) {
                        for (int b = 0; b < numberOfCategoryColumns; b++) {
                            double value = getAt(i, j, a, b);
                            norm2 += value * value;
                        }
                    }
                }
            }
        }

        return Math.sqrt(norm2);
    }


    @Override
    public double normInf() {
        double normInf = 0;

        if (isSparse()) {
            for (IndexedTensorValue x : this) {
                normInf = Math.max(normInf, Math.abs(x.getDoubleValue()));
            }
        } else {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    for (int a = 0; a < numberOfCategoryRows; a++) {
                        for (int b = 0; b < numberOfCategoryColumns; b++) {
                            normInf = Math.max(normInf, Math.abs(getAt(i, j, a, b)));
                        }
                    }
                }
            }
        }

        return normInf;
    }
}
