package com.antigenomics.pmem.representation.algebra.tensor;

import com.antigenomics.pmem.representation.algebra.Element;

public final class RealCategoricalTensorElement
        implements Element<RealCategoricalTensorElement, Double> {
    private final int index1, index2,
            category1, category2;
    private final double value;

    public RealCategoricalTensorElement(int index1, int index2,
                                        int category1, int index4,
                                        double value) {
        this.index1 = index1;
        this.index2 = index2;
        this.category1 = category1;
        this.category2 = index4;
        this.value = value;
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    public int getCategory1() {
        return category1;
    }

    public int getCategory2() {
        return category2;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Double getBoxedValue() {
        return value;
    }

    @Override
    public int compareTo(RealCategoricalTensorElement o) {
        int cmp = Integer.compare(index1, o.index1);

        if (cmp == 0) cmp = Integer.compare(index2, o.index2);
        if (cmp == 0) cmp = Integer.compare(category1, o.category1);
        if (cmp == 0) cmp = Integer.compare(category2, o.category2);

        return cmp;
    }
}
