package data_structure;

import model.Flashcard;
import model.LinkedNode;

import java.util.ArrayList;
import java.util.List;

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

    public void mergeSortByKey() {
        head = mergeSortByKey(head);
        LinkedNode current = head;
        tail = null;
        while (current != null) {
            tail = current;
            current = current.next;
        }
    }

    private LinkedNode mergeSortByKey(LinkedNode node) {
        if (node == null || node.next == null) {
            return node;
        }

        LinkedNode middle = getMiddle(node);
        LinkedNode nextOfMiddle = middle.next;
        middle.next = null;
        if (nextOfMiddle != null) nextOfMiddle.prev = null;

        LinkedNode left = mergeSortByKey(node);
        LinkedNode right = mergeSortByKey(nextOfMiddle);

        return sortedMergeByKey(left, right);
    }

    private LinkedNode getMiddle(LinkedNode node) {
        if (node == null) return node;
        LinkedNode slow = node, fast = node;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private LinkedNode sortedMergeByKey(LinkedNode a, LinkedNode b) {
        if (a == null) return b;
        if (b == null) return a;

        LinkedNode result;

        if (a.data.getKey().compareTo(b.data.getKey()) <= 0) {
            result = a;
            result.next = sortedMergeByKey(a.next, b);
            if (result.next != null) result.next.prev = result;
            result.prev = null;
        } else {
            result = b;
            result.next = sortedMergeByKey(a, b.next);
            if (result.next != null) result.next.prev = result;
            result.prev = null;
        }
        return result;
    }


    public List<Flashcard> toList() {
        List<Flashcard> result = new ArrayList<>();
        LinkedNode node = getHead();
        while (node != null) {
            result.add(node.data);
            node = node.next;
        }
        return result;
    }
}
