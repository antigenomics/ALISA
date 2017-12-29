package com.antigenomics.alisa.statistics;

import com.antigenomics.alisa.RandomSampler;
import com.antigenomics.alisa.estimator.mc.MonteCarloUtils;
import com.antigenomics.alisa.hamiltonian.Hamiltonian;
import com.antigenomics.alisa.hamiltonian.Representation;
import com.antigenomics.alisa.state.State;
import com.antigenomics.alisa.state.StateSpace;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// todo: random state generator and random sampler
public class ExactStateProbabilityDistribution<S extends State, R extends Representation> {
    private final Hamiltonian<S, R> hamiltonian;
    private final StateSpace<S> stateSpace;
    private final R parameters;
    private final double partitionFunctionValue;
    private ExactStateRandomSampler exactExactStateSampler = null;

    public ExactStateProbabilityDistribution(Hamiltonian<S, R> hamiltonian,
                                             R parameters,
                                             StateSpace<S> stateSpace) {
        this(hamiltonian, parameters, stateSpace,
                hamiltonian.hasTheoreticalPartitionFunction());
    }

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
            partitionFunctionValue = stateSpace.getStateSequence().parallelStream()
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

    public double getPartitionFunctionValue() {
        return partitionFunctionValue;
    }

    public ExactStateRandomSampler createExactStateSampler() {
        if (exactExactStateSampler == null) {
            if (stateSpace.getSize() > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("State space too large for exact sampler.");
            }
            exactExactStateSampler = new ExactStateRandomSampler();
        }
        return exactExactStateSampler;
    }

    public class ExactStateRandomSampler implements RandomSampler<S> {
        private final List<StateProbability<S>> stateProbabilities;

        private ExactStateRandomSampler() {
            this.stateProbabilities = computeProbabilities(stateSpace.getStateSequence().parallelStream());
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExactStateProbabilityDistribution<?, ?> that = (ExactStateProbabilityDistribution<?, ?>) o;

        if (Double.compare(that.partitionFunctionValue, partitionFunctionValue) != 0) return false;
        if (!hamiltonian.equals(that.hamiltonian)) return false;
        if (!stateSpace.equals(that.stateSpace)) return false;
        return parameters.equals(that.parameters);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = hamiltonian.hashCode();
        result = 31 * result + stateSpace.hashCode();
        result = 31 * result + parameters.hashCode();
        temp = Double.doubleToLongBits(partitionFunctionValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
