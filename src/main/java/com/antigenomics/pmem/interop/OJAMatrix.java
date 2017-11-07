package com.antigenomics.pmem.interop;

import com.antigenomics.pmem.representation.LinearSpaceObject;
import com.antigenomics.pmem.representation.Matrix;
import com.antigenomics.pmem.representation.MutableLinearSpaceObject;
import org.ojalgo.matrix.store.MatrixStore;

public class OJAMatrix implements Matrix<OJAMatrix> {
    private final MatrixStore<Double> innerMatrix;

    public OJAMatrix(MatrixStore<Double> innerMatrix) {
        this.innerMatrix = innerMatrix;
    }

    @Override
    public OJAMatrix multiply(OJAMatrix other) {
        return new OJAMatrix(innerMatrix.multiply(other.innerMatrix));
    }

    @Override
    public OJAMatrix transpose() {
        return new OJAMatrix(innerMatrix.transpose());
    }

    @Override
    public LinearSpaceObject<OJAMatrix> plus(LinearSpaceObject<OJAMatrix> other) {
        return new OJAMatrix(innerMatrix.add(((OJAMatrix) other).innerMatrix));
    }

    @Override
    public LinearSpaceObject<OJAMatrix> multiply(double scalar) {
        return new OJAMatrix(innerMatrix.multiply(scalar));
    }

    @Override
    public MutableLinearSpaceObject<OJAMatrix> toMutable() {
        return null;
    }

    @Override
    public double norm() {
        return innerMatrix.norm();
    }

    @Override
    public OJAMatrix asSymmetric() {
        return this;
    }
}
