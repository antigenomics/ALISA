package com.antigenomics.pmem.representation.algebra.tensor;

import com.antigenomics.pmem.representation.algebra.Element;

public final class RealCategoricalTensorElement
        implements Element<RealCategoricalTensorElement, Double> {
    private final int category1, category2,
            index1, index2;
    private final double value;

    public RealCategoricalTensorElement(int category1, int category2,
                                        int index1, int index2,
                                        double value) {
        this.category1 = category1;
        this.category2 = category2;
        this.index1 = index1;
        this.index2 = index2;
        this.value = value;
    }

    public int getCategory1() {
        return category1;
    }

    public int getCategory2() {
        return category2;
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
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
        int cmp = Integer.compare(category1, o.category1);

        if (cmp == 0) cmp = Integer.compare(category2, o.category2);
        if (cmp == 0) cmp = Integer.compare(index1, o.index1);
        if (cmp == 0) cmp = Integer.compare(index2, o.index2);

        return cmp;
    }
}
