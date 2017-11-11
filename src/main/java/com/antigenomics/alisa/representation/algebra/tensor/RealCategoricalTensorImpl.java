package com.antigenomics.alisa.representation.algebra.tensor;

import com.antigenomics.alisa.representation.MutableLinearSpaceObject;

import java.util.Iterator;

public class RealCategoricalTensorImpl
        implements RealCategoricalTensor {

    @Override
    public double bilinearForm(CategoricalVector a, CategoricalVector b) {
        double res = 0;
        for (int i = 0; i < a.getSize(); i++) {
            for (int j = 0; j < b.getSize(); j++) {
                res += getAt(a.getCategory(i), b.getCategory(j), i, j);
            }
        }
        return res;
    }

    @Override
    public double bilinearForm(CategoricalVector a) {
        if (isStrictlySymmetric()) {
            return bilinearForm(a, a);
        }
        throw new IllegalArgumentException("Symmetric invocation of a bilinear form with non-symmetric representation.");
    }

    @Override
    public RealCategoricalTensor plus(RealCategoricalTensor other) {
        return null;
    }

    @Override
    public RealCategoricalTensor multiply(double scalar) {
        return null;
    }

    @Override
    public MutableLinearSpaceObject<RealCategoricalTensor> toMutable() {
        return null;
    }

    @Override
    public double norm1() {
        return 0;
    }

    @Override
    public double norm2() {
        return 0;
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public int getEffectiveSize() {
        return 0;
    }

    @Override
    public int getNumberOfCategories1() {
        return 0;
    }

    @Override
    public int getNumberOfCategories2() {
        return 0;
    }

    @Override
    public int getNumberOfIndices1() {
        return 0;
    }

    @Override
    public int getNumberOfIndices2() {
        return 0;
    }

    @Override
    public double getAt(int a, int b, int i, int j) {
        return 0;
    }

    @Override
    public boolean isStrictlySymmetric() {
        return false;
    }

    @Override
    public boolean isStrictlySemiSymmetric() {
        return false;
    }

    @Override
    public Iterator<RealCategoricalTensorElement> iterator() {
        return null;
    }
}
