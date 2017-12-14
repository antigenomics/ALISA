package com.antigenomics.alisa.estimator.mc;

import com.antigenomics.alisa.state.State;

public interface StateMutator<S extends State> {
    S mutate(S state);
}
