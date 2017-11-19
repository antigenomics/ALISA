package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Iterator;

public class FullTensor
        extends Tensor {
    private final double[] elements;

    protected FullTensor(double[] elements,
                         int numberOfRows, int numberOfColumns,
                         int numberOfCategoryRows, int numberOfCategoryColumns) {
        this(elements, numberOfRows, numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns, false);
    }

    public FullTensor(double[] elements,
                      int numberOfRows, int numberOfColumns,
                      int numberOfCategoryRows, int numberOfCategoryColumns,
                      boolean safe) {
        super(numberOfRows, numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns);
        assert elements.length == numberOfRows * numberOfColumns * numberOfCategoryRows * numberOfCategoryColumns;
        if (safe) {
            this.elements = Arrays.copyOf(elements, elements.length);
        } else {
            this.elements = elements;
        }
    }

    public double getAt(int i, int j, int a, int b) {
        return elements[LinearAlgebraUtils.getFullTensorIndex(i, j, a, b,
                numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns)];
    }

    @Override
    protected double getAt(int linearIndex) {
        return elements[linearIndex];
    }

    @Override
    protected double bilinearFormUnchecked(CategoricalVector a, CategoricalVector b) {
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

    @Override
    protected CategoricalVector linearFormUnchecked(CategoricalVector b) {
        //todo
        throw new NotImplementedException();
    }

    @Override
    protected Tensor addUnchecked(Tensor other) {
        //todo
        throw new NotImplementedException();
    }

    @Override
    protected void addInplaceUnchecked(Tensor other) {
        //todo
        throw new NotImplementedException();
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public double getAt(int... indices) {
        //todo
        throw new NotImplementedException();
    }

    @Override
    public int getEffectiveSize() {
        //todo
        throw new NotImplementedException();
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
    public void multiplyInplace(double scalar) {
        LinearAlgebraUtils.scaleInplace(elements, scalar);
    }

    @Override
    public Tensor multiply(double scalar) {
        return new FullTensor(LinearAlgebraUtils.scale(elements, scalar),
                numberOfRows, numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns);
    }

    @Override
    public Tensor deepCopy() {
        return new FullTensor(Arrays.copyOf(elements, elements.length),
                numberOfRows, numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns);
    }

    @Override
    public Iterator<IndexedTensorValue> iterator() {
        //todo
        throw new NotImplementedException();
    }
}
