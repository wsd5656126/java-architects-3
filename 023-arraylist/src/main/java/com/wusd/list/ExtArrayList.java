package com.wusd.list;

import java.util.Arrays;

public class ExtArrayList<E> implements ExtList<E> {
    private Object[] elementData;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;

    public ExtArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("初始容量不能小于0");
        }
        elementData = new Object[initialCapacity];
    }
    public ExtArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public void add(E e) {
        ensureExplicitCapactity(size + 1);
        elementData[size++] = e;
    }

    public void add(int index, Object o) {
        ensureExplicitCapactity(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = o;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        rangeCheck(index);
        return (E) elementData[index];
    }

    public E remove(int index) {
        E obj = get(index);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        elementData[--size] = null;
        return obj;
    }

    public boolean remove(Object o) {
        for (int i = 0; i < elementData.length; i++) {
            Object val = elementData[i];
            if (val.equals(o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("索引越界");
    }
    private void ensureExplicitCapactity(int minCapacity) {
        if (size == elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity < 0)
                newCapacity = minCapacity;
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }
}
