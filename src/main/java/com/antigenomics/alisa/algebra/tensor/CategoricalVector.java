package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.Container;
import com.antigenomics.alisa.algebra.LinearAlgebraUtils;
import com.antigenomics.alisa.algebra.VectorSpace;
import com.antigenomics.alisa.encoding.Encoding;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.getTriangularTensorLength;

/**
 * note, this is not a linear object
 */
public class CategoricalVector
        implements VectorSpace<CategoricalVector, Tensor>,
        Container<IndexedCategoryWeight, CategoricalVector>,
        Encoding {
    protected final CategoryWeightPair[] elements;
    protected final int numberOfCategories;

    protected CategoricalVector(CategoryWeightPair[] elements,
                                int numberOfCategories) {
        this(elements, numberOfCategories, false);
    }

    public CategoricalVector(CategoryWeightPair[] elements,
                             int numberOfCategories, boolean safe) {
        CategoryWeightPair[] elementsSafe;
        if (safe) {
            elementsSafe = new CategoryWeightPair[elements.length];
            for (int i = 0; i < elements.length; i++) {
                if (elements[i].getCategory() > numberOfCategories) {
                    throw new IllegalArgumentException("Vector category index is greater than the " +
                            "pre-defined number of categories.");
                }
                elementsSafe[i] = elements[i];
            }
        } else {
            elementsSafe = elements;
        }
        this.elements = elementsSafe;
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

    public CategoryWeightPair getAt(int index) {
        return elements[index];
    }

    @Override
    public double getAt(int... indices) {
        if (indices.length != 2)
            throw new IllegalArgumentException();
        CategoryWeightPair categoryWeightPair = elements[indices[0]];

        return categoryWeightPair.getCategory() == indices[1] ? categoryWeightPair.getWeight() : 0;
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
            CategoryWeightPair categoryWeightPair1 = elements[i],
                    categoryWeightPair2 = b.elements[i];
            if (categoryWeightPair1.getCategory() == categoryWeightPair2.getCategory()) {
                res += categoryWeightPair1.getWeight() * categoryWeightPair2.getWeight();
            }
        }

        return res;
    }

    @Override
    public Tensor outerProduct(CategoricalVector b) {
        double[] tensorElements = new double[elements.length * b.elements.length *
                numberOfCategories * b.numberOfCategories];

        for (int i = 0; i < elements.length; i++) {
            CategoryWeightPair val1 = elements[i];
            for (int j = 0; j < b.elements.length; j++) {
                CategoryWeightPair val2 = b.elements[i];
                tensorElements[LinearAlgebraUtils.getFullTensorIndex(i, j,
                        val1.getCategory(), val2.getCategory(),
                        b.elements.length, numberOfCategories, b.numberOfCategories)] = val1.getWeight() * val2.getWeight();
            }
        }

        return new FullTensor(tensorElements, elements.length,
                b.elements.length, numberOfCategories, b.numberOfCategories);
    }

    @Override
    public Tensor expand() {
        double[] tensorElements = new double[getTriangularTensorLength(elements.length, numberOfCategories)];

        for (int i = 0; i < elements.length; i++) {
            CategoryWeightPair val1 = elements[i];
            for (int j = 0; j <= i; j++) {
                CategoryWeightPair val2 = elements[j];
                tensorElements[LinearAlgebraUtils.getTriangularTensorIndex(i, j,
                        val1.getCategory(), val2.getCategory(), elements.length)] = val1.getWeight() * val2.getWeight();
            }
        }

        return new SymmetricTensor(tensorElements, elements.length, numberOfCategories);
    }

    @Override
    public Iterator<IndexedCategoryWeight> iterator() {
        List<IndexedCategoryWeight> elementList = new ArrayList<>();

        for (int i = 0; i < elements.length; i++) {
            CategoryWeightPair categoryWeightPair = elements[i];
            elementList.add(new IndexedCategoryWeight(i,
                    categoryWeightPair.getCategory(), categoryWeightPair.getWeight()));
        }

        return elementList.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoricalVector that = (CategoricalVector) o;

        if (numberOfCategories != that.numberOfCategories) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(elements);
        result = 31 * result + numberOfCategories;
        return result;
    }
}
