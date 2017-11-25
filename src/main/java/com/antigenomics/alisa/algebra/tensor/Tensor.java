package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.Container;
import com.antigenomics.alisa.algebra.LinearAlgebraUtils;
import com.antigenomics.alisa.algebra.LinearSpaceObject;
import com.antigenomics.alisa.algebra.VectorMapping;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public abstract class Tensor
        implements LinearSpaceObject<Tensor>,
        VectorMapping<CategoricalVector, Tensor>, Container<IndexedTensorValue, Tensor> {
    /* internal storage */
    protected final double[] elements;

    /* true tensor shape */
    protected final int numberOfRows, numberOfColumns,
            numberOfCategoryRows, numberOfCategoryColumns;
    protected final boolean symmetric, semiSymmetric;

    protected Tensor(double[] elements,
                     int numberOfRows, int numberOfColumns,
                     int numberOfCategoryRows, int numberOfCategoryColumns,
                     boolean symmetric, boolean semiSymmetric,
                     boolean safe) {
        if (safe) {
            this.elements = Arrays.copyOf(elements, elements.length);
        } else {
            this.elements = elements;
        }
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfCategoryRows = numberOfCategoryRows;
        this.numberOfCategoryColumns = numberOfCategoryColumns;
        this.symmetric = symmetric;
        this.semiSymmetric = semiSymmetric;
    }

    protected abstract Tensor withElements(double[] elements);

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
        return symmetric;
    }

    public boolean isSemiSymmetric() {
        return semiSymmetric;
    }

    protected double bilinearFormUnchecked(CategoricalVector a, CategoricalVector b) {
        if (a.getLength() > b.getLength()) {
            return bilinearFormUnchecked(b, a);
        }

        double res = 0;

        for (int i = 0; i < numberOfRows; i++) {
            CategoryWeightPair categoryWeightPair1 = a.getAt(i);
            for (int j = 0; j < numberOfColumns; j++) {
                CategoryWeightPair categoryWeightPair2 = b.getAt(i);
                res += getAt(i, j, categoryWeightPair1.getCategory(), categoryWeightPair2.getCategory()) *
                        categoryWeightPair1.getWeight() * categoryWeightPair2.getWeight();
            }
        }

        return res;
    }

    protected double bilinearFormUnchecked(CategoricalVector a) {
        return bilinearFormUnchecked(a, a);
    }

    protected Tensor addUnchecked(Tensor other) {
        double[] newElements = Arrays.copyOf(elements, elements.length);
        for (int i = 0; i < newElements.length; i++) {
            newElements[i] += other.getAt(i);
        }
        return withElements(newElements);
    }

    protected void addInplaceUnchecked(Tensor other) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] += other.getAt(i);
        }
    }

    @Override
    public double bilinearForm(CategoricalVector a, CategoricalVector b) {
        checkSizeMatchLeft(a);
        checkSizeMatchRight(b);
        return bilinearFormUnchecked(a, b);
    }

    @Override
    public CategoricalVector map(CategoricalVector b) {
        throw new NotImplementedException();
    }


    @Override
    public double bilinearForm(CategoricalVector a) {
        checkSizeMatchLeft(a);
        checkSizeMatchRight(a);
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

    @Override
    public Tensor multiply(double scalar) {
        return withElements(LinearAlgebraUtils.scale(elements, scalar));
    }

    @Override
    public void multiplyInplace(double scalar) {
        LinearAlgebraUtils.scaleInplace(elements, scalar);
    }

    public double getAt(int rowIndex, int columnIndex,
                        int rowCategory, int columnCategory) {
        return elements[getLinearIndex(rowIndex, columnIndex, rowCategory, columnCategory)];
    }

    protected double getAt(int linearIndex) {
        return elements[linearIndex];
    }

    protected abstract int getLinearIndex(int rowIndex, int columnIndex,
                                          int rowCategory, int columnCategory);

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public Tensor asSparse() {
        throw new NotImplementedException();
    }

    @Override
    public Tensor asDense() {
        return deepCopy();
    }

    @Override
    public Tensor deepCopy() {
        return withElements(Arrays.copyOf(elements, elements.length));
    }

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
        if (symmetric != other.symmetric ||
                semiSymmetric != other.semiSymmetric ||
                numberOfRows != other.numberOfRows ||
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

        for (double element : elements) {
            norm1 += Math.abs(element);
        }

        return norm1;
    }

    @Override
    public double norm2() {
        double norm2 = 0;

        for (double value : elements) {
            norm2 += value * value;
        }

        return Math.sqrt(norm2);
    }

    @Override
    public double normInf() {
        double normInf = 0;

        for (double element : elements) {
            normInf = Math.max(normInf, Math.abs(element));
        }

        return normInf;
    }
}
