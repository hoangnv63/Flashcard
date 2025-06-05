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
        LinkedNode node = head;
        while (node != null) {
            if (node.data.getKey().equalsIgnoreCase(key)) return node;
            node = node.next;
        }
        return null;
    }

    public void add(Flashcard flashcard) {
        flashcard.setUploadNumber(size + 1);
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

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private LinkedNode getMiddle(LinkedNode start) {
        if (start == null) return null;
        LinkedNode slow = start, fast = start;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private LinkedNode mergeSort(LinkedNode start, boolean ascending, String criteria) {
        if (start == null || start.next == null) return start;

        LinkedNode middle = getMiddle(start);
        LinkedNode rightStart = middle.next;
        middle.next = null;
        if (rightStart != null) rightStart.prev = null;

        LinkedNode leftSorted = mergeSort(start, ascending, criteria);
        LinkedNode rightSorted = mergeSort(rightStart, ascending, criteria);

        return merge(leftSorted, rightSorted, ascending, criteria);
    }

    private LinkedNode merge(LinkedNode leftNode, LinkedNode rightNode, boolean ascending, String criteria) {
        if (leftNode == null) return rightNode;
        if (rightNode == null) return leftNode;

        int cmp = switch (criteria) {
            case "key" -> leftNode.data.getKey().compareToIgnoreCase(rightNode.data.getKey());
            case "description" -> leftNode.data.getDescription().compareToIgnoreCase(rightNode.data.getDescription());
            case "uploadNumber" -> Integer.compare(leftNode.data.getUploadNumber(), rightNode.data.getUploadNumber());
            default -> 0;
        };
        if (!ascending) cmp = -cmp;

        if (cmp <= 0) {
            leftNode.next = merge(leftNode.next, rightNode, ascending, criteria);
            if (leftNode.next != null) leftNode.next.prev = leftNode;
            leftNode.prev = null;
            return leftNode;
        } else {
            rightNode.next = merge(leftNode, rightNode.next, ascending, criteria);
            if (rightNode.next != null) rightNode.next.prev = rightNode;
            rightNode.prev = null;
            return rightNode;
        }
    }

    public void sortByKey(boolean ascending) {
        head = mergeSort(head, ascending, "key");
        fixTail();
    }

    public void sortByDescription(boolean ascending) {
        head = mergeSort(head, ascending, "description");
        fixTail();
    }

    public void sortByUploadNumber(boolean ascending) {
        head = mergeSort(head, ascending, "uploadNumber");
        fixTail();
    }

    private void fixTail() {
        LinkedNode current = head;
        if (current == null) {
            tail = null;
            return;
        }
        current.prev = null;
        while (current.next != null) {
            current.next.prev = current;
            current = current.next;
        }
        tail = current;
    }

    public List<Flashcard> toList() {
        List<Flashcard> result = new ArrayList<>();
        LinkedNode node = head;
        while (node != null) {
            result.add(node.data);
            node = node.next;
        }
        return result;
    }
}
