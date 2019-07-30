package com.wusd.list;

public interface ExtList<E> {
    void add(E e);
    int size();
    E get(int index);
}
