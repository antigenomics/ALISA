package com.antigenomics.alisa.representation.algebra.tensor;

import com.antigenomics.alisa.representation.algebra.Element;

public final class CategoricalVectorElement
        implements Element<CategoricalVectorElement, Integer> {
    private final int index;
    private final int category;

    public CategoricalVectorElement(int index, int category) {
        this.index = index;
        this.category = category;
    }

    public int getIndex() {
        return index;
    }

    public double getCategory() {
        return category;
    }

    @Override
    public int compareTo(CategoricalVectorElement o) {
        return Integer.compare(index, o.index);
    }

    @Override
    public Integer getBoxedValue() {
        return category;
    }
}
