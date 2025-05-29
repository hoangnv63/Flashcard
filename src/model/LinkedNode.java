package model;

public class LinkedNode {
    public Flashcard data;
    public LinkedNode prev;
    public LinkedNode next;

    public LinkedNode(Flashcard data) {
        this.data = data;
    }
}