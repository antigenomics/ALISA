package com.antigenomics.alisa.representation.algebra.matrix;

import com.antigenomics.alisa.representation.LinearSpaceObjectUtils;
import com.antigenomics.alisa.representation.MutableLinearSpaceObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class SparseVector
        extends SafeRealVector {
    private static final RealVectorElement EMPTY = new RealVectorElement(-1, 0);

    private final LinkedList<RealVectorElement> elements;
    private final int size;

    public SparseVector(double[] elements) {
        this.elements = new LinkedList<>();
        this.size = elements.length;

        for (int i = 0; i < size; i++) {
            double value = elements[i];

            if (value != 0) {
                this.elements.addLast(new RealVectorElement(i, value));
            }
        }
    }

    public SparseVector(LinkedList<RealVectorElement> elements, int size) {
        this.elements = elements;
        this.size = size;
    }

    @Override
    protected double dotProductUnchecked(RealVector b) {
        double res = 0;

        if (b.isSparse()) {
            Iterator<RealVectorElement> iterA = this.iterator(),
                    iterB = b.iterator();

            if (iterA.hasNext() && iterB.hasNext()) {
                RealVectorElement ai = iterA.next(),
                        bj = iterB.next();

                loop:
                while (true) {
                    switch (ai.compareTo(bj)) {
                        case -1:
                            if (iterA.hasNext()) {
                                ai = iterA.next();
                            } else {
                                break loop;
                            }
                            break;
                        case 0:
                            res += ai.getValue() * bj.getValue();
                            if (iterA.hasNext() && iterB.hasNext()) {
                                ai = iterA.next();
                                bj = iterB.next();
                            } else {
                                break loop;
                            }
                            break;
                        case 1:
                            if (iterB.hasNext()) {
                                bj = iterB.next();
                            } else {
                                break loop;
                            }
                            break;
                    }
                }
            }
        } else {
            for (RealVectorElement x : elements) {
                res += x.getValue() * b.getAt(x.getIndex());
            }
        }

        return res;
    }

    @Override
    protected RealVector plusUnchecked(RealVector other) {
        if (other.isSparse()) {
            LinkedList<RealVectorElement> elements = new LinkedList<>();

            Iterator<RealVectorElement> iterA = this.iterator(),
                    iterB = other.iterator();

            if (iterA.hasNext() && iterB.hasNext()) {
                RealVectorElement ai = iterA.next(),
                        bj = iterB.next();

                loop:
                while (true) {
                    switch (ai.compareTo(bj)) {
                        case -1:
                            elements.addLast(ai);

                            if (iterA.hasNext()) {
                                ai = iterA.next();
                            } else {
                                break loop;
                            }
                            break;
                        case 0:
                            elements.addLast(new RealVectorElement(ai.getIndex(),
                                    ai.getValue() + bj.getValue()));

                            if (iterA.hasNext() && iterB.hasNext()) {
                                ai = iterA.next();
                                bj = iterB.next();
                            } else {
                                break loop;
                            }
                            break;
                        case 1:
                            elements.addLast(bj);

                            if (iterB.hasNext()) {
                                bj = iterB.next();
                            } else {
                                break loop;
                            }
                            break;
                    }
                }
            }

            return new SparseVector(elements, size);
        } else {
            return other.plus(this);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public double getAt(int index) {
        // very slow
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        return elements.stream()
                .filter(x -> x.getIndex() == index)
                .findFirst()
                .orElse(EMPTY).getValue();
    }

    @Override
    public RealMatrix outerProduct(RealVector b) {
        return null;
    }

    @Override
    public RealVector multiply(double scalar) {
        return new SparseVector(elements
                .stream()
                .map(x -> new RealVectorElement(x.getIndex(), x.getValue() * scalar))
                .collect(Collectors.toCollection(LinkedList::new)),
                size);
    }

    @Override
    public MutableLinearSpaceObject<RealVector> toMutable() {
        // todo: vectors cannot be mutable - implement exception class
        throw new NotImplementedException();
    }

    @Override
    public double norm1() {
        double res = 0;
        for (RealVectorElement x : elements) {
            res += Math.abs(x.getValue());
        }
        return res;
    }

    @Override
    public double norm2() {
        double res = 0;
        for (RealVectorElement x : elements) {
            res += x.getValue() * x.getValue();
        }
        return Math.sqrt(res);
    }

    @Override
    public boolean isSparse() {
        return true;
    }

    @Override
    public int getEffectiveSize() {
        return elements.size();
    }

    @Override
    public Iterator<RealVectorElement> iterator() {
        return elements.iterator();
    }
}