package com.antigenomics.alisa.state;

import com.antigenomics.alisa.entities.Entity;

import java.io.Serializable;

public interface State<E extends Entity> extends Serializable {
    E getValue(final int layer);

    int getNumberOfLayers();

    double getDegeneracy();
}
