package com.antigenomics.alisa.representation.algebra.tensor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CategoricalVectorImpl
        implements CategoricalVector {
    private final int[] categoryArray;
    private final int numberOfCategories;
    private List<CategoricalVectorElement> elementList = null;

    public CategoricalVectorImpl(int[] categoryArray, int numberOfCategories) {
        this.categoryArray = categoryArray;
        this.numberOfCategories = numberOfCategories;
    }

    @Override
    public int getSize() {
        return categoryArray.length;
    }

    @Override
    public int getNumberOfCategories() {
        return numberOfCategories;
    }

    @Override
    public int getCategory(int index) {
        return categoryArray[index];
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public int getEffectiveSize() {
        return numberOfCategories * categoryArray.length;
    }

    @Override
    public double dotProduct(CategoricalVector b) {
        if (getSize() != b.getSize() ||
                getNumberOfCategories() != b.getNumberOfCategories()) {
            throw new IllegalArgumentException("Incompatible vectors");
        }

        double res = 0;

        for (int i = 0; i < categoryArray.length; i++) {
            if (categoryArray[i] == b.getCategory(i))
                res++;
        }

        return res;
    }

    @Override
    public RealCategoricalTensor outerProduct(CategoricalVector b) {

    }

    @Override
    public RealCategoricalTensor expand() {

    }

    @Override
    public Iterator<CategoricalVectorElement> iterator() {
        // very slow
        if (elementList == null) {
            elementList = new ArrayList<>();

            for (int i = 0; i < categoryArray.length; i++) {
                elementList.add(new CategoricalVectorElement(i, categoryArray[i]));
            }
        }

        return elementList.iterator();
    }
}
