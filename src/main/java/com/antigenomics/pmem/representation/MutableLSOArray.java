package com.antigenomics.pmem.representation;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MutableLSOArray<O extends ImmutableLinearSpaceObject<O>>
        extends SafeMutableLinearSpaceObject<ImmutableLSOArray<O>> {
    protected final List<MutableLinearSpaceObject<O>> mutableObjectList = new ArrayList<>();

    public MutableLSOArray(@NotNull final List<? extends O> objectList) {
        for (O obj : objectList)
            this.mutableObjectList.add(obj.toMutable());
    }

    public MutableLinearSpaceObject<O> get(int index) {
        return mutableObjectList.get(index);
    }

    public int size() {
        return mutableObjectList.size();
    }

    @Override
    protected boolean isCompatible(ImmutableLSOArray<O> other) {
        return mutableObjectList.size() == other.size();
    }

    @Override
    protected boolean isCompatible(MutableLinearSpaceObject<ImmutableLSOArray<O>> other) {
        return other instanceof MutableLSOArray && mutableObjectList.size() == ((MutableLSOArray) other).size();
    }

    @Override
    protected void plusInplaceUnchecked(ImmutableLSOArray<O> other) {
        for (int i = 0; i < mutableObjectList.size(); i++)
            mutableObjectList.get(i).plusInplace(other.get(i));
    }

    @Override
    protected void minusInplaceUnchecked(ImmutableLSOArray<O> other) {
        for (int i = 0; i < mutableObjectList.size(); i++)
            mutableObjectList.get(i).minusInplace(other.get(i));
    }

    @Override
    protected void plusInplaceUnchecked(MutableLinearSpaceObject<ImmutableLSOArray<O>> other) {
        for (int i = 0; i < mutableObjectList.size(); i++)
            mutableObjectList.get(i).plusInplace(((MutableLSOArray<O>) other).get(i));
    }

    @Override
    protected void minusInplaceUnchecked(MutableLinearSpaceObject<ImmutableLSOArray<O>> other) {
        for (int i = 0; i < mutableObjectList.size(); i++)
            mutableObjectList.get(i).minusInplace(((MutableLSOArray<O>) other).get(i));
    }

    @Override
    public void multiplyInplace(double scalar) {
        for (MutableLinearSpaceObject<O> obj : mutableObjectList)
            obj.multiplyInplace(scalar);
    }

    @Override
    public ImmutableLSOArray<O> toImmutable() {
        final List<O> objectList = new ArrayList<>();
        for (MutableLinearSpaceObject<O> obj : mutableObjectList)
            objectList.add(obj.toImmutable());

        return new ImmutableLSOArray<>(objectList);
    }

    @Override
    public double norm1() {
        return LSOArrayUtils.norm1(mutableObjectList);
    }

    @Override
    public double norm2() {
        return LSOArrayUtils.norm2(mutableObjectList);
    }
}
