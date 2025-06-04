
# Flashcard Learning Application

This Java-based flashcard application is designed to facilitate efficient and engaging self-study. It allows users to create, manage, search, sort, and quiz themselves on custom flashcards. Built with key data structures and algorithms, it emphasizes both usability and algorithmic performance.

---

## Features

- **Create & Edit Flashcards**: Users can input a key (term) and description (definition) to form a flashcard.
- **Efficient Sorting**: Flashcards can be sorted by key, description, or upload order using merge sort on a custom doubly linked list.
- **Prefix Search**: Quickly locate flashcards with a specific prefix using a Trie structure.
- **Shuffling for Learning & Quiz**: Fisher-Yates algorithm is used to randomize cards in quizzes and self-study sessions.
- **Interactive Quiz Mode**: Test knowledge with a quiz interface that gives instant feedback and tracks scores.
- **Confirmation Dialogs**: Protect against accidental changes with confirm prompts before edits or deletions.

---

## Data Structures

| Structure            | Purpose                                      |
|----------------------|----------------------------------------------|
| Doubly Linked List   | Manages flashcard storage and sorting        |
| Trie                 | Supports fast prefix searching               |
| ArrayList            | Holds and shuffles cards for quizzes/study   |

---

## Algorithms

| Algorithm                | Use Case                      | Time Complexity |
|--------------------------|-------------------------------|-----------------|
| Merge Sort               | Sort cards by criteria        | O(n log n)      |
| Prefix Search (Trie)     | Efficient key lookup          | O(m), m = prefix length |
| Fisher-Yates Shuffle     | Randomize flashcard order     | O(n)            |
| Add/Delete (LinkedList)  | Flashcard insertion/removal   | O(1)            |

---

## Educational Benefits

- Encourages active recall and spaced repetition through custom quizzes.
- Supports self-paced learning with flexible organization and shuffling.
- Provides visual and textual reinforcement for deeper memory retention.

---

## Future Development

- **User Accounts & Persistence**: Connect to a database to save user progress and flashcard sets.
- **Topic-Based Sets**: Allow users to create flashcard lists by topic or subject.
- **Flashcard Sharing**: Enable users to share sets with others for collaborative learning.
- **Multimedia Support**: Add image/audio capabilities to support various learning styles.
- **Web Deployment**: Develop a web version to increase accessibility across devices.

---

## Technologies Used

- **Java Swing**: For the GUI interface
- **Custom Java Classes**: For implementing data structures and logic
- **Object-Oriented Design**: Ensures modularity and scalability
