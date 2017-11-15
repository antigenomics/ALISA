package com.antigenomics.alisa.encoding;

import com.antigenomics.alisa.entities.Entity;

public interface State<E extends Entity> {
    E getValue(final int layer);

    int getNumberOfLayers();
}
