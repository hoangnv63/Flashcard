package ui;

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

public class FlashcardUI {
    private JFrame frame;
    private JLabel cardLabel, indexLabel;
    private JTextField searchField;
    private JButton prevButton, nextButton;
    private boolean showingKey = true;

    private final FlashcardLinkedList flashcardLinkedList = new FlashcardLinkedList();
    private final FlashcardTrie flashcardTrie = new FlashcardTrie();
    private LinkedNode currentNode;

    public void initUI() {
        setLookAndFeel();
        initializeFrame();
        setupHeader();
        setupSidebar();
        setupCardDisplay();
        setupNavigation();

        loadFlashcardsFromFile("src/cards/flashcards.txt");

        currentNode = flashcardLinkedList.getHead();
        updateCardDisplay();

        frame.setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus is not available. Default Look and Feel will be used.");
        }
    }

    private void initializeFrame() {
        frame = new JFrame("My Flashcards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
    }

    private void setupHeader() {
        JLabel titleLabel = new JLabel("MY FLASHCARDS", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setBounds(340, 20, 400, 40);
        frame.add(titleLabel);

        searchField = new JTextField();
        searchField.setBounds(100, 80, 700, 40);
        frame.add(searchField);

        JButton searchButton = createButton("Search", 820, 80, 120, 40);
        searchButton.addActionListener(e -> handleSearch());
        frame.add(searchButton);
    }

    private void handleSearch() {
        String prefix = searchField.getText().trim();
        if (prefix.isEmpty()) {
            showMessage("Please enter a prefix to search.");
            return;
        }

        List<String> suggestions = flashcardTrie.suggest(prefix);
        if (suggestions.isEmpty()) {
            showMessage("No matching flashcards found.");
        } else {
            StringBuilder sb = new StringBuilder("Suggestions:\n");
            for (String s : suggestions) sb.append("- ").append(s).append("\n");
            showMessage(sb.toString());

            LinkedNode node = flashcardLinkedList.findByKey(suggestions.get(0));
            if (node != null) {
                setCurrentNode(node);
                updateCardDisplay();
            }
        }
    }

    private void setupSidebar() {
        String[] labels = {"Add card", "Delete card", "Edit card", "Arrange cards", "Quiz"};

        for (int i = 0; i < labels.length; i++) {
            JButton button = createSidebarButton(labels[i], 150 + i * 60);
            addSidebarAction(button, labels[i]);
            frame.add(button);
        }
    }

    private JButton createSidebarButton(String text, int y) {
        JButton button = new JButton(text);
        button.setBounds(50, y, 200, 50);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        return button;
    }

    private void addSidebarAction(JButton button, String label) {
        switch (label) {
            case "Add card" -> button.addActionListener(e -> FlashcardActions.addCard(frame, flashcardLinkedList, this));
            case "Delete card" -> button.addActionListener(e -> FlashcardActions.deleteCard(frame, flashcardLinkedList, this));
            case "Edit card" -> button.addActionListener(e -> FlashcardActions.editCard(frame, flashcardLinkedList, this));
        }
    }

    private void setupCardDisplay() {
        cardLabel = new JLabel("", JLabel.CENTER);
        cardLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        cardLabel.setOpaque(true);
        cardLabel.setBackground(Color.WHITE);
        cardLabel.setBounds(300, 150, 700, 400);
        cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showingKey = !showingKey;
                updateCardDisplay();
            }
        });
        frame.add(cardLabel);
    }

    private void setupNavigation() {
        prevButton = createNavButton("src/assets/prevbtn.png", 500);
        nextButton = createNavButton("src/assets/nextbtn.png", 750);

        prevButton.addActionListener(e -> {
            LinkedNode prevNode = flashcardLinkedList.getPrev(currentNode);
            if (prevNode != null) {
                currentNode = prevNode;
                showingKey = true;
                updateCardDisplay();
            }
        });

        nextButton.addActionListener(e -> {
            LinkedNode nextNode = flashcardLinkedList.getNext(currentNode);
            if (nextNode != null) {
                currentNode = nextNode;
                showingKey = true;
                updateCardDisplay();
            }
        });

        frame.add(prevButton);
        frame.add(nextButton);

        indexLabel = new JLabel("", JLabel.CENTER);
        indexLabel.setBounds(550, 570, 200, 50);
        indexLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        frame.add(indexLabel);
    }

    private JButton createNavButton(String path, int x) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);

        JButton button = new JButton(icon);
        button.setBounds(x, 570, 50, 50);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
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
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    Flashcard card = new Flashcard(parts[0].trim(), parts[1].trim());
                    flashcardLinkedList.add(card);
                    flashcardTrie.insert(card.getKey(), card.getDescription());
                }
            }
        } catch (IOException e) {
            showMessage("Error loading flashcards: " + e.getMessage());
        }
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        return button;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}
