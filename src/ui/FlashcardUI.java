package ui;

import data_structure.FlashcardArrayList;
import data_structure.FlashcardLinkedList;
import data_structure.FlashcardTrie;
import model.Flashcard;
import model.LinkedNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static ui.FlashcardActions.shuffleCards;

public class FlashcardUI {
    private JFrame frame;
    private JLabel cardLabel, indexLabel;
    private JTextField searchField;
    private JButton prevButton, nextButton;
    private boolean showingKey = true;

    private JComboBox<String> sortCriteriaBox;
    private JComboBox<String> sortOrderBox;

    private final FlashcardLinkedList flashcardLinkedList = new FlashcardLinkedList();
    private final FlashcardArrayList flashcardArrayList = new FlashcardArrayList();
    private final FlashcardTrie flashcardTrie = new FlashcardTrie();
    private LinkedNode currentNode;

    public JFrame getFrame() {
        return frame;
    }

    public void initUI() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus not available.");
        }

        frame = new JFrame("My Flashcards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        setupHeader();
        setupSidebar();
        setupCardDisplay();
        setupNavigation();

        loadFlashcardsFromFile("src/cards/flashcards.txt");
        currentNode = flashcardLinkedList.getHead();
        updateCardDisplay();

        frame.setVisible(true);
    }

    private void setupHeader() {
        JLabel titleLabel = new JLabel("MY FLASHCARDS", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        titleLabel.setBounds(340, 30, 400, 60);
        frame.add(titleLabel);

        searchField = new JTextField();
        searchField.setBounds(100, 100, 780, 40); // wider
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        frame.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(890, 100, 100, 40);
        searchButton.setBackground(Color.BLACK);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> {
            String prefix = searchField.getText().trim();
            FlashcardActions.searchCard(frame, flashcardLinkedList, flashcardTrie, this, prefix);
        });
        frame.add(searchButton);

        setupSortingControls();
    }

    private void setupSortingControls() {
        int y = 160;
        JLabel sortingLabel = new JLabel("Sort cards by:");
        sortingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        sortingLabel.setBounds(100, y, 120, 30);
        frame.add(sortingLabel);

        String[] criteriaOptions = {"Key", "Description", "Upload Number"};
        String[] orderOptions = {"Ascending", "Descending"};

        sortCriteriaBox = new JComboBox<>(criteriaOptions);
        sortCriteriaBox.setBounds(240, y, 120, 30);
        frame.add(sortCriteriaBox);

        sortOrderBox = new JComboBox<>(orderOptions);
        sortOrderBox.setBounds(380, y, 120, 30);
        frame.add(sortOrderBox);

        JButton applySortButton = new JButton("Sort");
        applySortButton.setBounds(520, y, 120, 30);
        applySortButton.setBackground(Color.BLACK);
        applySortButton.setForeground(Color.WHITE);
        applySortButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        applySortButton.addActionListener(e -> {
            String criteria = (String) sortCriteriaBox.getSelectedItem();
            String order = (String) sortOrderBox.getSelectedItem();
            applySort(criteria, order);
        });
        frame.add(applySortButton);
    }

    private void setupSidebar() {
        String[] labels = {"Add card", "Delete card", "Edit card", "Quiz", "Shuffle cards"};

        for (int i = 0; i < labels.length; i++) {
            JButton button = new JButton(labels[i]);
            button.setBounds(50, 210 + i * 60, 200, 50);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 20));
            button.setFocusPainted(false);
            frame.add(button);

            switch (labels[i]) {
                case "Add card":
                    button.addActionListener(e -> FlashcardActions.addCard(frame, flashcardLinkedList, this));
                    break;
                case "Delete card":
                    button.addActionListener(e -> FlashcardActions.deleteCard(frame, flashcardLinkedList, this));
                    break;
                case "Edit card":
                    button.addActionListener(e -> FlashcardActions.editCard(frame, flashcardLinkedList, this));
                    break;
                case "Quiz":
                    button.addActionListener(e -> {
                        List<Flashcard> cards = flashcardLinkedList.toList();
                        if (cards.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "No flashcards available for quiz.");
                        } else {
                            QuizDialog quizDialog = new QuizDialog(frame, cards);
                            quizDialog.setVisible(true);
                        }
                    });
                    break;
                case "Shuffle cards":
                    button.addActionListener(e -> {
                        shuffleCards(frame, flashcardLinkedList, this);
                        JOptionPane.showMessageDialog(frame, "Cards shuffled!");
                    });
                    break;
            }
        }
    }

    private void applySort(String criteria, String order) {
        boolean ascending = order.equals("Ascending");
        switch (criteria) {
            case "Key":
                flashcardLinkedList.sortByKey(ascending);
                break;
            case "Description":
                flashcardLinkedList.sortByDescription(ascending);
                break;
            case "Upload Number":
                flashcardLinkedList.sortByUploadNumber(ascending);
                break;
            default:
                JOptionPane.showMessageDialog(frame, "Invalid sort criteria selected.");
                return;
        }
        currentNode = flashcardLinkedList.getHead();
        showingKey = true;
        updateCardDisplay();
    }

    private void setupCardDisplay() {
        cardLabel = new JLabel("", JLabel.CENTER);
        cardLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        cardLabel.setOpaque(true);
        cardLabel.setBackground(Color.WHITE);
        cardLabel.setBounds(300, 210, 700, 360);
        cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showingKey = !showingKey;
                updateCardDisplay();
            }
        });
        frame.add(cardLabel);
    }

    private void setupNavigation() {
        int y = 590;
        prevButton = new JButton("◀");
        prevButton.setBounds(500, y, 50, 50);
        prevButton.setBackground(Color.WHITE);
        prevButton.setForeground(Color.BLACK);
        prevButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        prevButton.setFocusPainted(false);
        prevButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        prevButton.addActionListener(e -> {
            LinkedNode prevNode = flashcardLinkedList.getPrev(currentNode);
            if (prevNode != null) {
                currentNode = prevNode;
                showingKey = true;
                updateCardDisplay();
            }
        });
        frame.add(prevButton);

        nextButton = new JButton("▶");
        nextButton.setBounds(750, y, 50, 50);
        nextButton.setBackground(Color.WHITE);
        nextButton.setForeground(Color.BLACK);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        nextButton.setFocusPainted(false);
        nextButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        nextButton.addActionListener(e -> {
            LinkedNode nextNode = flashcardLinkedList.getNext(currentNode);
            if (nextNode != null) {
                currentNode = nextNode;
                showingKey = true;
                updateCardDisplay();
            }
        });
        frame.add(nextButton);

        indexLabel = new JLabel("", JLabel.CENTER);
        indexLabel.setBounds(570, y, 150, 50);
        indexLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        frame.add(indexLabel);
    }

    public void updateCardDisplay() {
        if (currentNode == null) {
            cardLabel.setText("No cards available");
            indexLabel.setText("0/0");
            return;
        }

        Flashcard card = currentNode.data;
        cardLabel.setText("<html><body style='width: 420px; text-align: center;'>" +
                (showingKey ? card.getKey() : card.getDescription()) +
                "</body></html>");

        int index = flashcardLinkedList.indexOf(currentNode);
        indexLabel.setText((index + 1) + "/" + flashcardLinkedList.getSize());
    }

    public LinkedNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(LinkedNode node) {
        currentNode = node;
    }

    public void loadFlashcardsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int uploadCounter = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String description = parts[1].trim();
                    Flashcard card = new Flashcard(key, description, uploadCounter++);
                    flashcardLinkedList.add(card);
                    flashcardTrie.insert(key, description);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading flashcards: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}