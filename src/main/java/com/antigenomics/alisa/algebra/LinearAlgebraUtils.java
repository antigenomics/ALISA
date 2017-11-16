package com.antigenomics.alisa.algebra;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LinearAlgebraUtils {
    public static double norm1(double[] elements) {
        double res = 0;

        for (double x : elements)
            res += Math.abs(x);

        return res;
    }

    public static double norm2(double[] elements) {
        double res = 0;

        for (double x : elements)
            res += x * x;

        return Math.sqrt(res);
    }

    public static double[] scale(double[] elements, double scalar) {
        double[] newElements = Arrays.copyOf(elements, elements.length);

        for (int i = 0; i < elements.length; i++) {
            newElements[i] *= scalar;
        }

        return newElements;
    }

    public static void scaleInplace(double[] elements, double scalar) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] *= scalar;
        }
    }

    public static <V extends IndexedValue<V>> boolean checkSortedNoDuplicates(Iterable<V> elementList) {
        Iterator<V> iter = elementList.iterator();

        if (iter.hasNext()) {
            V prevIndexedValue = elementList.iterator().next();
            for (V indexedValue : elementList) {
                int cmp = prevIndexedValue.compareTo(indexedValue);

                if (cmp >= 0) {
                    return false;
                }

                prevIndexedValue = indexedValue;
            }
        }

        return true;
    }

    public static <O extends LinearSpaceObject<O>> boolean equalUpToTol(LinearSpaceObject<O> o1,
                                                                        LinearSpaceObject<O> o2) {
        return equalUpToTol(o1, o2, 1e-16);
    }

    public static <O extends LinearSpaceObject<O>> boolean equalUpToTol(LinearSpaceObject<O> o1,
                                                                        LinearSpaceObject<O> o2,
                                                                        double tol) {
        return o1.add(o2.multiply(-1.0)).norm2() <= tol;
    }

    public static <V extends IndexedValue<V>> void scale(List<V> storage,
                                                         Iterable<V> input,
                                                         double scalar) {
        for (V v : input) {
            storage.add(v.scale(scalar));
        }
    }

    public static <V extends IndexedValue<V>> void combineAdd(List<V> storage,
                                                              Iterable<V> first,
                                                              Iterable<V> second) {
        Iterator<V> iterA = first.iterator(),
                iterB = second.iterator();

        if (iterA.hasNext() && iterB.hasNext()) {
            V ai = iterA.next(), bj = iterB.next();

            loop:
            while (true) {
                switch (ai.compareTo(bj)) {
                    case -1:
                        storage.add(ai);

                        if (iterA.hasNext()) {
                            ai = iterA.next();
                        } else {
                            break loop;
                        }
                        break;
                    case 0:
                        storage.add(ai.add(bj));

                        if (iterA.hasNext() && iterB.hasNext()) {
                            ai = iterA.next();
                            bj = iterB.next();
                        } else {
                            break loop;
                        }
                        break;
                    case 1:
                        storage.add(bj);

                        if (iterB.hasNext()) {
                            bj = iterB.next();
                        } else {
                            break loop;
                        }
                        break;
                }
            }
        }
    }

    public static int computeNumberOfFullMatrixRows(int numberOfElements, int numberOfColumns) {
        if (numberOfElements % numberOfColumns == 0) {
            return numberOfElements / numberOfColumns;
        }
        throw new IllegalArgumentException("Wrong number of elements for a " +
                "dense matrix with " + numberOfColumns + " columns");
    }

    public static int getFullMatrixIndex(int i, int j,
                                         /*int n,*/ int m) {
        return i * m + j;
    }

    public static int getTriangularMatrixIndex(int i, int j
                                         /*int n, int m*/) {
        if (j > i) {
            return getTriangularMatrixIndex(j, i);
        } else {
            return getTriangularMatrixLength(i) + j;
        }
    }

    public static int getFullTensorIndex(int i, int j, int a, int b,
                                         /*int n,*/ int m, int k, int l) {
        int i1 = getFullMatrixIndex(i, j, m),
                i2 = getFullMatrixIndex(a, b, l);

        return getFullMatrixIndex(i1, i2, k * l);
    }

    public static int getSemiTriangularTensorIndex(int i, int j, int a, int b,
                                                   /*int n,*/ int m, /*int k,*/ int l) {
        int i1 = getFullMatrixIndex(i, j, m),
                i2 = getTriangularMatrixIndex(a, b);

        return getFullMatrixIndex(i1, i2, getTriangularMatrixLength(l));
    }

    public static int getTriangularTensorIndex(int i, int j, int a, int b,
                                                   /*int n, int m, int k,*/ int l) {
        int i1 = getTriangularMatrixIndex(i, j),
                i2 = getTriangularMatrixIndex(a, b);

        return getFullMatrixIndex(i1, i2, getTriangularMatrixLength(l));
    }

    public static int getTriangularMatrixLength(int n) {
        return (n * (n + 1)) / 2;
    }

    public static int getTriangularMatrixSize(int length) {
        int n = (int) ((-1 + Math.sqrt(1 + 8 * length)) / 2);

        if (getTriangularMatrixLength(n) != length) {
            throw new IllegalArgumentException("Wrong number of elements for a " +
                    "triangular matrix.");
        }

        return n;
    }
}
