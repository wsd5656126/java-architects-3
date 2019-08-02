package com.wusd;

public interface ExtMap<K, V> {
    V put(K k, V v);
    V get(K k);
    int size();
    interface Entry<K, V> {
        K getKey();
        V getValue();
        V setValue(V v);
    }
}
