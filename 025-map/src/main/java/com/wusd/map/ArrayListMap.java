package com.wusd.map;

import com.wusd.list.ExtArrayList;

public class ArrayListMap<K, V> {
    private static class Entry<K, V> {
        public K key;
        public V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    private ExtArrayList<Entry<K, V>> entries = new ExtArrayList<>();
    public ArrayListMap() {}
    public V get(K key) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K, V> entry = entries.get(i);
            if (key.equals(entry.key)) {
                return entry.value;
            }
        }
        return null;
    }

    public void put(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value);
        entries.add(entry);
    }
}
