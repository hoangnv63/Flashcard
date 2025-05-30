package data_structure;

import java.util.*;
import model.TrieNode;
import model.Flashcard;

public class Trie {
    private final TrieNode root = new TrieNode();

    public void insert(String key, String description) {
        TrieNode node = root;
        for (char ch : key.toCharArray()) {
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        // Insert flashcard at the end of the word
        node.isEndOfWord = true;
        node.flashcard = new Flashcard(key, description);
    }

    public List<String> suggest(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) {
                System.out.println("No match found for prefix: " + prefix);
                return new ArrayList<>();
            }
        }

        List<String> results = new ArrayList<>();
        dfs(node, results);

        System.out.println("Suggestions for prefix '" + prefix + "': " + results);
        return results;
    }

    private void dfs(TrieNode node, List<String> results) {
        if (node.isEndOfWord && node.flashcard != null) {
            results.add(node.flashcard.getKey());
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            dfs(entry.getValue(), results);
        }
    }

}
