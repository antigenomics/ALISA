package com.antigenomics.alisa.representation;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ImmutableLSOArray<O extends ImmutableLinearSpaceObject<O>>
        extends SafeImmutableLinearSpaceObject<ImmutableLSOArray<O>> {
    private final List<? extends O> objectList;

    public ImmutableLSOArray(@NotNull final List<? extends O> objectList) {
        this.objectList = objectList;
    }

    public int size() {
        return objectList.size();
    }

    public O get(int index) {
        return objectList.get(index);
    }

    @Override
    public ImmutableLSOArray<O> plusUnchecked(@NotNull final ImmutableLSOArray<O> other) {
        ArrayList<O> newObjectList = new ArrayList<>(objectList.size());

        for (int i = 0; i < objectList.size(); i++)
            newObjectList.add(objectList.get(i).plus(other.objectList.get(i)));

        return new ImmutableLSOArray<>(newObjectList);
    }

    @Override
    protected boolean isCompatible(ImmutableLSOArray<O> other) {
        return objectList.size() == other.objectList.size();
    }

    @Override
    public ImmutableLSOArray<O> multiply(final double scalar) {
        ArrayList<O> newObjectList = new ArrayList<>(objectList.size());

        for (O obj : objectList)
            newObjectList.add(obj.multiply(scalar));


        return new ImmutableLSOArray<>(newObjectList);
    }

    @Override
    public double norm1() {
        return LinearSpaceObjectUtils.norm1(objectList);
    }

    @Override
    public double norm2() {
        return LinearSpaceObjectUtils.norm2(objectList);
    }

    @Override
    public MutableLinearSpaceObject<ImmutableLSOArray<O>> toMutable() {
        return new MutableLSOArray<>(objectList);
    }
}