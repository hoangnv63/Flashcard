package model;

public class Flashcard {
    private String key;
    private String description;
    private int uploadNumber;  // New field for upload order

    public Flashcard(String key, String description, int uploadNumber) {
        this.key = key;
        this.description = description;
        this.uploadNumber = uploadNumber;
    }

    public Flashcard(String key, String description) {
        this(key, description, 0);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUploadNumber() {
        return uploadNumber;
    }

    public void setUploadNumber(int uploadNumber) {
        this.uploadNumber = uploadNumber;
    }
}
