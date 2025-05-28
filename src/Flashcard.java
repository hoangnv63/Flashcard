public class Flashcard {
    private String key;
    private String description;

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

    public void setKey(String newKey) {
        this.key = key;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
