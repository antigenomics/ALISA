package com.antigenomics.pmem.state;

import com.antigenomics.pmem.entities.Entity;

public interface State<E extends Entity> {
    E getValue(final int layer);

    int getNumberOfLayers();
}
