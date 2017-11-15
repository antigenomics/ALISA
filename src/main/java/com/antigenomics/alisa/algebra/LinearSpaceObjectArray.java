package com.antigenomics.alisa.algebra;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinearSpaceObjectArray<O extends LinearSpaceObject<O>>
        implements LinearSpaceObject<LinearSpaceObjectArray<O>>, Iterable<O> {
    private final List<O> objectList;

    public LinearSpaceObjectArray(final List<O> objectList) {
        this.objectList = objectList;
    }

    public int size() {
        return objectList.size();
    }

    public O get(int index) {
        return objectList.get(index);
    }

    @Override
    public LinearSpaceObjectArray<O> add(@NotNull final LinearSpaceObjectArray<O> other) {
        checkSizeMatch(other);
        final List<O> newObjectList = new ArrayList<>(objectList.size());

        for (int i = 0; i < objectList.size(); i++) {
            newObjectList.add(objectList.get(i).add(other.objectList.get(i)));
        }

        return new LinearSpaceObjectArray<>(newObjectList);
    }

    @Override
    public LinearSpaceObjectArray<O> multiply(final double scalar) {
        final List<O> newObjectList = new ArrayList<>(objectList.size());

        for (O obj : objectList) {
            newObjectList.add(obj.multiply(scalar));
        }

        return new LinearSpaceObjectArray<>(newObjectList);
    }

    @Override
    public double norm1() {
        double norm1 = 0;

        for (O obj : objectList) {
            norm1 += obj.norm1();
        }

        return norm1;
    }

    @Override
    public double norm2() {
        double norm2 = 0;

        for (O obj : objectList) {
            double value = obj.norm2();
            norm2 += value * value;
        }

        return Math.sqrt(norm2);
    }

    @Override
    public void addInplace(@NotNull LinearSpaceObjectArray<O> other) {
        checkSizeMatch(other);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).addInplace(other.objectList.get(i));
        }
    }

    @Override
    public void multiplyInplace(final double scalar) {
        objectList.forEach(x -> x.multiplyInplace(scalar));
    }

    @Override
    public LinearSpaceObjectArray<O> deepCopy() {
        final List<O> newObjectList = new ArrayList<>(objectList.size());

        for (O obj : objectList) {
            newObjectList.add(obj.deepCopy());
        }

        return new LinearSpaceObjectArray<>(newObjectList);
    }

    private void checkSizeMatch(@NotNull LinearSpaceObjectArray<O> other) {
        if (objectList.size() != other.objectList.size())
            throw new IllegalArgumentException("Array sizes don't match");
    }

    @Override
    public Iterator<O> iterator() {
        return objectList.iterator();
    }
}
