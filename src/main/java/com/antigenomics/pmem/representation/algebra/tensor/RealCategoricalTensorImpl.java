package com.antigenomics.pmem.representation.algebra.tensor;

import com.antigenomics.pmem.representation.MutableLinearSpaceObject;

import java.util.Iterator;

public class RealCategoricalTensorImpl
        implements RealCategoricalTensor {
    @Override
    public double bilinearForm(CategoricalVector a, CategoricalVector b) {
        return 0;
    }

    @Override
    public double bilinearForm(CategoricalVector a) {
        return 0;
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
    public int getNumberOfIndices1() {
        return 0;
    }

    @Override
    public int getNumberOfIndices2() {
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
    public double getAt(int i, int j, int a, int b) {
        return 0;
    }

    @Override
    public boolean isStrictlySymmetricByIndices() {
        return false;
    }

    @Override
    public boolean isStrictlySymmetricByCategories() {
        return false;
    }

    @Override
    public Iterator<RealCategoricalTensorElement> iterator() {
        return null;
    }
}
