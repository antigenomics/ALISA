package com.antigenomics.pmem.estimator.mc;

import com.antigenomics.pmem.state.State;

public interface StateMutator<S extends State> {
    S mutate(S state);
}
