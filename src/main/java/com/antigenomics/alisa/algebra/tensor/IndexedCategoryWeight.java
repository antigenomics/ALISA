package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.IndexedValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class IndexedCategoryWeight
        implements IndexedValue<IndexedCategoryWeight> {
    private final int index, category;
    private final double weight;

    public IndexedCategoryWeight(int index,
                                 int category,
                                 double weight) {
        this.index = index;
        this.category = category;
        this.weight = weight;
    }

    @Override
    public double getDoubleValue() {
        return weight;
    }

    @Override
    public int getIntValue() {
        return category;
    }

    public int getIndex() {
        return index;
    }

    public int getCategory() {
        return category;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public IndexedCategoryWeight add(IndexedCategoryWeight other) {
        return new IndexedCategoryWeight(index, category,
                weight + other.weight);
    }

    @Override
    public IndexedCategoryWeight scale(double scalar) {
        return new IndexedCategoryWeight(index, category,
                weight * scalar);
    }

    @Override
    public int compareTo(IndexedCategoryWeight o) {
        int cmp = Integer.compare(index, o.index);
        return cmp == 0 ? Integer.compare(category, o.category) : cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexedCategoryWeight that = (IndexedCategoryWeight) o;

        if (index != that.index) return false;
        if (category != that.category) return false;
        return Double.compare(that.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = index;
        result = 31 * result + category;
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
