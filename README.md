[![Build Status](https://travis-ci.org/antigenomics/ALISA.svg?branch=master)](https://travis-ci.org/antigenomics/ALISA)
[![Javadoc](https://img.shields.io/badge/javadoc-0.1.0-blue.svg)](https://antigenomics.github.io/ALISA/javadoc/overview-summary.html)

# ALgorithms for Inverse Statistical Approaches (ALISA)

Solving inverse problem of statistical mechanics: determining Hamiltonian representation parameters from an observed sample of states of the system.

> Inspired by [Stein et al. Inferring Pairwise Interactions from Biological Data Using Maximum-Entropy Probability Models](http://journals.plos.org/ploscompbiol/article?id=10.1371/journal.pcbi.1004182), [Nguyen et al. Inverse statistical problems: from the inverse Ising problem to data science](https://arxiv.org/pdf/1702.01522.pdf) and similar works

### Planned features:

* **Hamiltonians** Spin glass and multi-layer spin glass, pairwise interactions only.
* **Representations** Flexible Hamiltonian and state representations: sparse/dense vectors and matrices, simple tensors.
* **State encodings** Flexible system of state encodings: one-hot and k-mer encodings for protein sequences, real vector state representations based on AA physicochemical properties.
* **Models** Built-in models for protein regions, pairs and triples of interacting protein regions. This should cover T-cell receptor (TCR) selection, TCR chain pairing, TCR:peptide:MHC recognition, antibody affinity maturation, virus evolution, peptide:MHC binding, and protein-protein interaction (PPI) interfaces in general.
* **Model inference** Monte Carlo-based approaches and direct maximum likelihood approaches in case partition function can be simplified analytically. Optimized linear algebra methods and parallel inference algorithm implementation.
* **Hybrid models** Combining multiple independently-trained models
* **Predictors** TCR selection probabilities, TCR specificity prediction, virus fitness evaluation, PPI partner prediction, ...

# CONTRIBUTIONS WELCOME

Write me at my_name.my_surname@gmail.com if you know Java and are interested in statistical mechanics :)
