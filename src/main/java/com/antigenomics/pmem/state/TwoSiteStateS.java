package com.antigenomics.pmem.state;

import com.antigenomics.pmem.entities.Entity;

public class TwoSiteStateS<T extends Entity> extends TwoSiteStateNS<T, T> {
    public TwoSiteStateS(T firstValue, T secondValue) {
        super(firstValue, secondValue);
    }
}
