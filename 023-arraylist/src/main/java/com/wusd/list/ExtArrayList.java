package com.wusd.list;

import java.util.Arrays;

public class ExtArrayList<E> implements ExtList<E> {
    private Object[] elementData;
    private int size;
    private static final int DEFAULT_CAPICITY = 20;

    public ExtArrayList(int INITIAL_CAPICITY) {
        if (INITIAL_CAPICITY <= 0) {
            INITIAL_CAPICITY = DEFAULT_CAPICITY;
        }
        elementData = new Object[INITIAL_CAPICITY];
        size = 0;
    }

    public ExtArrayList() {
        this(DEFAULT_CAPICITY);
    }

    @Override
    public void add(E e) {
        if (size == elementData.length) {
            resize();
        }
        elementData[size++] = e;
    }

    private void resize() {
        elementData = Arrays.copyOf(elementData, size + (size >> 1));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        return (E) elementData[index];
    }
}
