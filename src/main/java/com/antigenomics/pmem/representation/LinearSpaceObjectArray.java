package com.antigenomics.pmem.representation;

import java.util.ArrayList;
import java.util.List;

public class LinearSpaceObjectArray<O extends LinearSpaceObject<O>>
        implements LinearSpaceObject<LinearSpaceObjectArray<O>> {
    private final List<? extends O> objectList;

    public LinearSpaceObjectArray(List<? extends O> objectList) {
        this.objectList = objectList;
    }

    public int size() {
        return objectList.size();
    }

    public O get(int index) {
        return objectList.get(index);
    }

    @Override
    public LinearSpaceObjectArray<O> plusUnchecked(LinearSpaceObjectArray<O> other) {
        ArrayList<O> newObjectList = new ArrayList<>(objectList.size());

        for (int i = 0; i < objectList.size(); i++) {
            newObjectList.add(objectList.get(i).plus(other.objectList.get(i)));
        }

        return new LinearSpaceObjectArray<>(newObjectList);
    }

    @Override
    public LinearSpaceObjectArray<O> multiply(double scalar) {
        ArrayList<O> newObjectList = new ArrayList<>(objectList.size());

        for (O obj : objectList) {
            newObjectList.add(obj.multiply(scalar));
        }

        return new LinearSpaceObjectArray<>(newObjectList);
    }

    @Override
    public double norm2() {
        double res = 0;
        for (O obj : objectList) {
            final double norm2 = obj.norm2();
            res += norm2 * norm2;
        }
        return Math.sqrt(res);
    }

    @Override
    public double norm1() {
        double res = 0;
        for (O obj : objectList) {
            res += obj.norm1();
        }
        return res;
    }

    @Override
    public boolean isEquivalent(LinearSpaceObjectArray<O> other) {
        return objectList.size() == other.objectList.size();
    }

    @Override
    public MutableLinearSpaceObject<LinearSpaceObjectArray<O>> toMutable() {
        return new CompanionMutableLinearSpaceObjectArray(copyInnerListAsMutable());
    }

    private ArrayList<MutableLinearSpaceObject<O>> copyInnerListAsMutable() {
        final ArrayList<MutableLinearSpaceObject<O>> mutableObjectList = new ArrayList<>();
        for (O obj : objectList) {
            mutableObjectList.add(obj.toMutable());
        }
        return mutableObjectList;
    }

    private ArrayList<O> copyInnerListAsImutable(ArrayList<MutableLinearSpaceObject<O>> mutableObjectList) {
        final ArrayList<O> mockImmutableObjectList = new ArrayList<>();
        for (MutableLinearSpaceObject<O> obj : mutableObjectList) {
            mockImmutableObjectList.add(obj.asImmutable());
        }
        return mockImmutableObjectList;
    }

    private class CompanionMutableLinearSpaceObjectArray
            extends LinearSpaceObjectArray<O>
            implements MutableLinearSpaceObject<LinearSpaceObjectArray<O>> {
        private final ArrayList<MutableLinearSpaceObject<O>> mutableObjectList;

        CompanionMutableLinearSpaceObjectArray(final ArrayList<MutableLinearSpaceObject<O>> mutableObjectList) {
            super(copyInnerListAsImutable(mutableObjectList));
            this.mutableObjectList = mutableObjectList;
        }

        @Override
        public void plusInplaceUnchecked(LinearSpaceObjectArray<O> other) {
            for (int i = 0; i < mutableObjectList.size(); i++) {
                mutableObjectList.get(i).plusInplace(other.objectList.get(i));
            }
        }

        @Override
        public void minusInplaceUnchecked(LinearSpaceObjectArray<O> other) {
            for (int i = 0; i < mutableObjectList.size(); i++) {
                mutableObjectList.get(i).minusInplace(other.objectList.get(i));
            }
        }

        @Override
        public void multiplyInplace(double scalar) {
            for (MutableLinearSpaceObject<O> obj : mutableObjectList) {
                obj.multiplyInplace(scalar);
            }
        }

        @Override
        public LinearSpaceObjectArray<O> toImmutable() {
            final ArrayList<O> trueImmutableObjectList = new ArrayList<>();
            for (MutableLinearSpaceObject<O> obj : mutableObjectList) {
                trueImmutableObjectList.add(obj.toImmutable());
            }
            return new LinearSpaceObjectArray<>(trueImmutableObjectList);
        }
    }
}