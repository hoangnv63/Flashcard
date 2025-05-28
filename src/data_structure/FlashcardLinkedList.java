package data_structure;

import model.Flashcard;
import model.FlashcardNode;

public class FlashcardLinkedList {
    private FlashcardNode head;
    private FlashcardNode tail;
    private int size = 0;

    public FlashcardNode getHead() {
        return head;
    }

    public FlashcardNode getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

    public FlashcardNode getPrev(FlashcardNode current) {
        if (current == null) return null;
        return current.prev;
    }

    public FlashcardNode getNext(FlashcardNode current) {
        if (current == null) return null;
        return current.next;
    }

    public int indexOf(FlashcardNode node) {
        int index = 0;
        FlashcardNode current = head;
        while (current != null) {
            if (current == node) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    public void add(Flashcard flashcard) {
        FlashcardNode node = new FlashcardNode(flashcard);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    public void delete(FlashcardNode node) {
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
