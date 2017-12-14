package com.antigenomics.alisa.statistics;

import com.antigenomics.alisa.Sampler;
import com.antigenomics.alisa.estimator.mc.MonteCarloUtils;
import com.antigenomics.alisa.hamiltonian.Hamiltonian;
import com.antigenomics.alisa.hamiltonian.Representation;
import com.antigenomics.alisa.state.State;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ExactStateProbabilityDistribution<S extends State, R extends Representation> {
    private final Hamiltonian<S, R> hamiltonian;
    private final StateSpace<S> stateSpace;
    private final R parameters;
    private final double partitionFunctionValue;
    private ExactStateSampler exactExactStateSampler = null;

    public ExactStateProbabilityDistribution(Hamiltonian<S, R> hamiltonian,
                                             R parameters,
                                             StateSpace<S> stateSpace,
                                             boolean computeTheoreticalPartitionFunction) {
        this.hamiltonian = hamiltonian;
        this.parameters = parameters;
        this.stateSpace = stateSpace;

        if (computeTheoreticalPartitionFunction) {
            if (!hamiltonian.hasTheoreticalPartitionFunction()) {
                throw new IllegalArgumentException("No theoretical partition function for this hamiltonian.");
            }
            partitionFunctionValue = hamiltonian.computePartitionFunction(parameters);
        } else {
            partitionFunctionValue = stateSpace.getStateGenerator().parallelStream()
                    .mapToDouble(x -> x.getDegeneracy() * computeExpE(x)).sum();
        }
    }

    private double computeExpE(S state) {
        return Math.exp(hamiltonian.computeEnergy(state, parameters));
    }

    public List<StateProbability<S>> computeProbabilities(List<S> states) {
        return computeProbabilities(states.parallelStream());
    }

    private List<StateProbability<S>> computeProbabilities(Stream<S> stateStream) {
        return stateStream
                .map(x -> new StateProbability<>(x, computeExpE(x) / partitionFunctionValue))
                .collect(Collectors.toList());
    }

    public Hamiltonian<S, R> getHamiltonian() {
        return hamiltonian;
    }

    public StateSpace<S> getStateSpace() {
        return stateSpace;
    }

    public R getParameters() {
        return parameters;
    }

    public ExactStateSampler createExactStateSampler() {
        if (exactExactStateSampler == null) {
            if (stateSpace.getSize() > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("State space too large for exact sampler.");
            }
            exactExactStateSampler = new ExactStateSampler();
        }
        return exactExactStateSampler;
    }

    public class ExactStateSampler implements Sampler<S> {
        private final List<StateProbability<S>> stateProbabilities;

        private ExactStateSampler() {
            this.stateProbabilities = computeProbabilities(stateSpace.getStateGenerator().parallelStream());
        }

        @Override
        public S sample() {
            double p = MonteCarloUtils.nextDouble();
            double pSum = 0;

            for (StateProbability<S> stateProbability : stateProbabilities) {
                pSum += stateProbability.getFullProbability();
                if (pSum >= p) {
                    return stateProbability.getState();
                }
            }

            return null;
        }

        @Override
        public List<S> sample(int count) {
            return IntStream.range(0, count).parallel()
                    .mapToObj(x -> sample())
                    .collect(Collectors.toList());
        }
    }

    // todo: random state generator and random sampler
}
