package com.antigenomics.pmem.state;

import com.antigenomics.pmem.entities.Entity;

public interface State<T extends Entity> {
    T getValue(final int site);

    int getNumberOfSites();
}
