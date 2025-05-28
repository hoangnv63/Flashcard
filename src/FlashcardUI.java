import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlashcardUI {
    private JFrame frame;
    private JLabel cardLabel, indexLabel;
    private JTextField searchField;
    private JButton prevButton, nextButton;
    private boolean showingKey = true;

    private final FlashcardList flashcardList = new FlashcardList();
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
        flashcardList.add(new Flashcard("Java", "A high-level programming language."));
        flashcardList.add(new Flashcard("Swing", "Java GUI toolkit."));
        flashcardList.add(new Flashcard("Polymorphism", "Ability to take many forms."));

        currentNode = flashcardList.getHead();
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
                    button.addActionListener(e -> FlashcardActions.addCard(frame, flashcardList, this));
                    break;
                case "Delete card":
                    button.addActionListener(e -> FlashcardActions.deleteCard(frame, flashcardList, this));
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
        prevButton = new JButton("←");
        prevButton.setBounds(400, 580, 100, 40);
        prevButton.addActionListener(e -> {
            if (currentNode != null && currentNode.prev != null) {
                currentNode = currentNode.prev;
                showingKey = true;
                updateCardDisplay();
            }
        });
        frame.add(prevButton);

        nextButton = new JButton("→");
        nextButton.setBounds(700, 580, 100, 40);
        nextButton.addActionListener(e -> {
            if (currentNode != null && currentNode.next != null) {
                currentNode = currentNode.next;
                showingKey = true;
                updateCardDisplay();
            }
        });
        frame.add(nextButton);

        indexLabel = new JLabel("", JLabel.CENTER);
        indexLabel.setBounds(480, 580, 200, 40);
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

        int index = flashcardList.indexOf(currentNode);
        indexLabel.setText((index + 1) + "/" + flashcardList.getSize());
    }

    public FlashcardNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(FlashcardNode node) {
        currentNode = node;
    }
}