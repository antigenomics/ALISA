package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.Container;
import com.antigenomics.alisa.algebra.VectorSpace;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Note that categorical vector doesn't extend linear object. Operations such as
 * addition and multiplication are meaningless for a dense vector of category indices.
 */
public class CategoricalVector
        implements VectorSpace<CategoricalVector, Tensor>, Container<IndexedCategory, CategoricalVector> {
    private final int[] elements;
    private final int numberOfCategories;

    public CategoricalVector(int[] elements, int numberOfCategories) {
        this.elements = elements;
        this.numberOfCategories = numberOfCategories;
    }

    public int getLength() {
        return elements.length;
    }

    public int getNumberOfCategories() {
        return numberOfCategories;
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public double getAt(int... indices) {
        if (indices.length != 1)
            throw new IllegalArgumentException();
        return elements[indices[0]];
    }

    @Override
    public int getEffectiveSize() {
        return elements.length;
    }

    @Override
    public CategoricalVector asSparse() {
        throw new NotImplementedException();
    }

    @Override
    public CategoricalVector asDense() {
        return deepCopy();
    }

    @Override
    public CategoricalVector deepCopy() {
        return new CategoricalVector(Arrays.copyOf(elements, elements.length),
                numberOfCategories);
    }

    @Override
    public double dotProduct(CategoricalVector b) {
        if (elements.length != b.elements.length ||
                numberOfCategories != b.numberOfCategories) {
            throw new IllegalArgumentException();
        }
        double res = 0;

        for (int i = 0; i < elements.length; i++) {

            if (elements[i] == b.elements[i]) {
                res++;
            }
        }

        return res;
    }

    @Override
    public Tensor outerProduct(CategoricalVector b) {

    }

    @Override
    public Tensor expand() {

    }

    @Override
    public Iterator<IndexedCategory> iterator() {
        List<IndexedCategory> elementList = new ArrayList<>();

        for (int i = 0; i < elements.length; i++) {
            elementList.add(new IndexedCategory(i, elements[i]));
        }

        return elementList.iterator();
    }
}
