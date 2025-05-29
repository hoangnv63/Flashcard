package data_structure;

import model.Flashcard;
import model.LinkedNode;

public class FlashcardLinkedList {
    private LinkedNode head;
    private LinkedNode tail;
    private int size = 0;

    public LinkedNode getHead() {
        return head;
    }

    public LinkedNode getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

    public LinkedNode getPrev(LinkedNode current) {
        if (current == null) return null;
        return current.prev;
    }

    public LinkedNode getNext(LinkedNode current) {
        if (current == null) return null;
        return current.next;
    }

    public int indexOf(LinkedNode node) {
        int index = 0;
        LinkedNode current = head;
        while (current != null) {
            if (current == node) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    public LinkedNode findByKey(String key) {
        LinkedNode node = getHead();
        while (node != null) {
            if (node.data.getKey().equalsIgnoreCase(key)) return node;
            node = node.next;
        }
        return null;
    }

    public void add(Flashcard flashcard) {
        LinkedNode node = new LinkedNode(flashcard);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    public void delete(LinkedNode node) {
        if (node == null) return;

        if (node == head && node == tail) {
            head = tail = null;
        } else if (node == head) {
            head = node.next;
            if (head != null) head.prev = null;
        } else if (node == tail) {
            tail = node.prev;
            if (tail != null) tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        size--;
    }
}
