package com.wusd;

import java.util.LinkedList;

public class ExtLinkedList<E> {
    public static void main(String[] args) {
//        LinkedList
        ExtLinkedList<String> linkedList = new ExtLinkedList<>();
        for (int i = 0; i < 100; i++) {
            linkedList.add("wusd" + i);
        }
        for (int i = 0; i < 100; i++) {
            System.out.println(i + ":" + linkedList.get(i));
        }
    }

    private static class Node<E> {
        Node<E> previous;
        E element;
        Node<E> next;

        public Node(E element, Node<E> previous, Node next) {
            this.previous = previous;
            this.element = element;
            this.next = next;
        }
    }

    private Node<E> sentrialNode;
    private int size;

    public ExtLinkedList() {
        sentrialNode = new Node<>((E) new Object(), null, null);
    }

    public E get(int index) {
        Node<E> resultNode = sentrialNode.next;
        for (int i = 0; i < index; i++) {
            resultNode = resultNode.next;
        }
        return resultNode.element;
    }

    public void add(E e) {
        Node<E> lastNode = sentrialNode;
        for (int i = 0; i < size; i++) {
            lastNode = lastNode.next;
        }
        lastNode.next = new Node<>(e, lastNode, null);
        size++;
    }

    public int size() {
        return size;
    }

}
