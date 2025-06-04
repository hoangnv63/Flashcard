package data_structure;

import model.Flashcard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlashcardArrayList {
    private final List<Flashcard> flashcards = new ArrayList<>();

    public void add(Flashcard card) {
        flashcards.add(card);
    }

    public List<Flashcard> getAll() {
        return flashcards;
    }

    public void shuffle() {
        Random rand = new Random();
        for (int i = flashcards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);

            Flashcard temp = flashcards.get(i);
            flashcards.set(i, flashcards.get(j));
            flashcards.set(j, temp);
        }
    }
}
