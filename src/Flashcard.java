public class Flashcard {
    private final String key;
    private final String description;

    public Flashcard(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
