package com.wusd;

public class ExtHashMap<K, V> implements ExtMap<K, V> {
    Node<K, V>[] table = null;
    int size;
    float DEFAULT_LOAD_FACTOR = 0.5f;
    static int DEFAULT_INITIAL_CAPACITY = 16;

    private void resize() {
        //之前的两倍大小
        Node<K, V>[] newTable = new Node[DEFAULT_INITIAL_CAPACITY << 1];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> oldNode = table[i];
            while (oldNode != null) {
                table[i] = null;
                K oldK= oldNode.key;
                int index = tableIndex(oldK, newTable.length);
                Node<K, V> oldNext = oldNode.next;
                oldNext.next = newTable[index];
                newTable[index] = oldNode;
                oldNode = oldNext;
            }
        }
        table = newTable;
        DEFAULT_INITIAL_CAPACITY = newTable.length;
        newTable = null;
    }

    private int tableIndex(K k, int length) {
        return k.hashCode() % length;
    }

    @Override
    public V put(K k, V v) {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
        }
        if (size > (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY)) {
            resize();
        }

        int index = tableIndex(k, DEFAULT_INITIAL_CAPACITY);
        Node<K, V> node = table[index];
        if (node == null) {
            node = new Node<>(k, v, null);
            size++;
        } else {
            Node<K, V> newNode = node;
            while (newNode != null) {
                if (newNode.key.equals(k)) {
                    return newNode.setValue(v);
                } else {
                    if (newNode.next == null) {
                        node = new Node<>(k, v, node);
                        size++;
                    }
                }
                newNode = newNode.next;
            }
        }
        table[index] = node;
        return null;
    }

    @Override
    public V get(K k) {
        Node<K, V> node = getNode(table[tableIndex(k, DEFAULT_INITIAL_CAPACITY)], k);
        return node == null ? null : node.value;
    }

    public Node<K, V> getNode(Node<K, V> node, K k) {
        while (node != null) {
            if (node.key.equals(k)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    // 定义节点
    class Node<K, V> implements ExtMap.Entry<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            V oldValue = this.value;
            this.value = v;
            return oldValue;
        }
    }
}
