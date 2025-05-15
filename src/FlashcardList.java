public class FlashcardList {
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
}
