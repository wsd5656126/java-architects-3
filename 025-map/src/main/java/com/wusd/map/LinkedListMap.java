package com.wusd.map;

import com.wusd.ExtLinkedList;

import java.util.LinkedList;

public class LinkedListMap<K, V> {
    public static class Entry<K, V> {
        private K key;
        private V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
    private ExtLinkedList<Entry<K, V>>[] buckets = new ExtLinkedList[998];
    public void put(K key, V value) {
        int bucketIndex = bucketIndex(key);
        ExtLinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        if (bucket == null) {
            bucket = new ExtLinkedList<>();
        }
        boolean contain = false;
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                contain = true;
                break;
            }
        }
        if (!contain) {
            bucket.add(new Entry<>(key, value));
        }
    }

    public V get(K key) {
        int bucketIndex = bucketIndex(key);
        System.out.println("bucketIndex:" + bucketIndex);
        ExtLinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    private int bucketIndex(K key) {
        int hashCode = key.hashCode();
        int bucketIndex = hashCode % buckets.length;
        return bucketIndex;
    }

    public static void main(String[] args) {
        LinkedListMap<Integer, Integer> linkedListMap = new LinkedListMap<>();
        for (int i = 0; i < 10000; i++) {
            linkedListMap.put(i, i);
        }
        System.out.println();
    }
}
