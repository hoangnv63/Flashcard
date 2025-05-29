package model;
import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    public Map<Character, TrieNode> children = new HashMap<>();
    public boolean isEndOfWord = false;
    public Flashcard flashcard;

    public TrieNode() {}

    public TrieNode(Flashcard flashcard) {
        this.flashcard = flashcard;
        this.isEndOfWord = true;
    }
}

