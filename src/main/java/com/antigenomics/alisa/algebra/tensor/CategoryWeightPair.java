package com.antigenomics.alisa.algebra.tensor;

import java.io.Serializable;

public class CategoryWeightPair implements Serializable,
        Comparable<CategoryWeightPair> {
    private final int category;
    private final double weight;

    public CategoryWeightPair(int category, double weight) {
        this.category = category;
        this.weight = weight;
    }

    public CategoryWeightPair(int category) {
        this(category, 1.0);
    }

    public int getCategory() {
        return category;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryWeightPair that = (CategoryWeightPair) o;

        if (category != that.category) return false;
        return Double.compare(that.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = category;
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int compareTo(CategoryWeightPair o) {
        return Integer.compare(category, o.category);
    }
}
