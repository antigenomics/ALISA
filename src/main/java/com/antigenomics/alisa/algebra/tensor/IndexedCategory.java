package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.IndexedValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class IndexedCategory
        implements IndexedValue<IndexedCategory> {
    private final int index, category;

    public IndexedCategory(final int index, final int category) {
        this.index = index;
        this.category = category;
    }

    @Override
    public double getDoubleValue() {
        return category;
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

    @Override
    public IndexedCategory add(final IndexedCategory other) {
        throw new NotImplementedException();
    }

    @Override
    public IndexedCategory scale(double scalar) {
        throw new NotImplementedException();
    }

    @Override
    public int compareTo(final IndexedCategory o) {
        return Integer.compare(category, o.category);
    }
}
