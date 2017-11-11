package com.antigenomics.alisa.representation.algebra.tensor;

public abstract class SafeRealCategoricalTensor
        implements RealCategoricalTensor {
    protected abstract CategoricalTensorIndexing getIndexing();

    protected abstract double bilinearFormUnchecked(CategoricalVector a, CategoricalVector b);

    protected abstract double bilinearFormUnchecked(CategoricalVector a);

    protected abstract RealCategoricalTensor plusUnchecked(RealCategoricalTensor a);

    @Override
    public final double bilinearForm(CategoricalVector a, CategoricalVector b) {
        if (isCompatibleLeft(a) && isCompatibleRight(b))
            return bilinearFormUnchecked(a, b);
        else
            throw new IllegalArgumentException("Matrix " + this +
                    " is not compatible with left vector " + a + " and/or right vector " + b);
    }

    @Override
    public final double bilinearForm(CategoricalVector a) {
        if (!isSquare())
            throw new IllegalArgumentException("Matrix " + this +
                    " is not square.");

        if (isCompatibleLeft(a))
            return bilinearFormUnchecked(a);
        else
            throw new IllegalArgumentException("Matrix " + this +
                    " is not compatible with vector " + a);
    }

    @Override
    public final RealCategoricalTensor plus(RealCategoricalTensor other) {
        if (isCompatible(other))
            return plusUnchecked(other);
        else
            throw new IllegalArgumentException("Tensor " + this +
                    " is not compatible with tensor " + other);
    }

    private boolean isCompatibleLeft(CategoricalVector other) {
        return this.getNumberOfIndices1() == other.getSize() &&
                this.getNumberOfCategories1() == other.getNumberOfCategories();
    }

    private boolean isCompatibleRight(CategoricalVector other) {
        return this.getNumberOfIndices2() == other.getSize() &&
                this.getNumberOfCategories2() == other.getNumberOfCategories();
    }

    private boolean isCompatible(RealCategoricalTensor other) {
        return this.getNumberOfIndices1() == other.getNumberOfIndices1() &&
                this.getNumberOfIndices2() == other.getNumberOfIndices2() &&
                this.getNumberOfCategories1() == other.getNumberOfCategories1() &&
                this.getNumberOfCategories2() == other.getNumberOfCategories2();
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public int getEffectiveSize() {
        return getIndexing().getEffectiveSize();
    }

    @Override
    public int getNumberOfCategories1() {
        return getIndexing().getNumberOfCategories1();
    }

    @Override
    public int getNumberOfCategories2() {
        return getIndexing().getNumberOfCategories2();
    }

    @Override
    public int getNumberOfIndices1() {
        return getIndexing().getNumberOfIndices1();
    }

    @Override
    public int getNumberOfIndices2() {
        return getIndexing().getNumberOfIndices2();
    }

    @Override
    public boolean isStrictlySymmetric() {
        return getIndexing().isStrictlySymmetric();
    }

    @Override
    public boolean isStrictlySemiSymmetric() {
        return getIndexing().isStrictlySemiSymmetric();
    }
}
