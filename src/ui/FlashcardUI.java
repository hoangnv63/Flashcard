package ui;

import data_structure.FlashcardLinkedList;
import model.Flashcard;
import model.FlashcardNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlashcardUI {
    private JFrame frame;
    private JLabel cardLabel, indexLabel;
    private JTextField searchField;
    private JButton prevButton, nextButton;
    private boolean showingKey = true;

    private final FlashcardLinkedList flashcardLinkedList = new FlashcardLinkedList();
    private FlashcardNode currentNode;

    public void initUI() {
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

        frame = new JFrame("My Flashcards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        setupHeader();
        setupSidebar();
        setupCardDisplay();
        setupNavigation();

        // Sample flashcards
        flashcardLinkedList.add(new Flashcard("Java", "A high-level programming language."));
        flashcardLinkedList.add(new Flashcard("Swing", "Java GUI toolkit."));
        flashcardLinkedList.add(new Flashcard("Polymorphism", "Ability to take many forms."));

        currentNode = flashcardLinkedList.getHead();
        updateCardDisplay();

        frame.setVisible(true);
    }

    private void setupHeader() {
        JLabel titleLabel = new JLabel("MY FLASHCARDS", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setBounds(340, 20, 400, 40);
        frame.add(titleLabel);

        searchField = new JTextField();
        searchField.setBounds(100, 80, 700, 40);
        frame.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(820, 80, 120, 40);
        searchButton.setBackground(Color.BLACK);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        frame.add(searchButton);
    }

    private void setupSidebar() {
        String[] labels = {"Add card", "Delete card", "Edit card", "Arrange cards", "Quiz"};

        for (int i = 0; i < labels.length; i++) {
            JButton button = new JButton(labels[i]);
            button.setBounds(50, 150 + i * 60, 200, 50);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 20));
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

            }
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
        ImageIcon nextIcon = new ImageIcon("src/assets/nextbtn.png");
        ImageIcon prevIcon = new ImageIcon("src/assets/prevbtn.png");

        Image nextImage = nextIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        nextIcon = new ImageIcon(nextImage);

        Image prevImage = prevIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        prevIcon = new ImageIcon(prevImage);

        prevButton = new JButton(prevIcon);
        prevButton.setBounds(500, 570, 50, 50);
        prevButton.setBorderPainted(false);
        prevButton.setContentAreaFilled(false);
        prevButton.setFocusPainted(false);
        prevButton.setOpaque(false);
        prevButton.addActionListener(e -> {
            FlashcardNode prevNode = flashcardLinkedList.getPrev(currentNode);
            if (prevNode != null) {
                currentNode = prevNode;
                showingKey = true;
                updateCardDisplay();
            }
        });
        frame.add(prevButton);

        nextButton = new JButton(nextIcon);
        nextButton.setBounds(750, 570, 50, 50);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setFocusPainted(false);
        nextButton.setOpaque(false);
        nextButton.addActionListener(e -> {
            FlashcardNode nextNode = flashcardLinkedList.getNext(currentNode);
            if (nextNode != null) {
                currentNode = nextNode;
                showingKey = true;
                updateCardDisplay();
            }
        });
        frame.add(nextButton);

        indexLabel = new JLabel("", JLabel.CENTER);
        indexLabel.setBounds(550, 570, 200, 50);
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

    public FlashcardNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(FlashcardNode node) {
        currentNode = node;
    }
}