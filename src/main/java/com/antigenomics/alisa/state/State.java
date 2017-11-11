package com.antigenomics.alisa.state;

import com.antigenomics.alisa.entities.Entity;

public interface State<E extends Entity> {
    E getValue(final int layer);

    int getNumberOfLayers();
}
