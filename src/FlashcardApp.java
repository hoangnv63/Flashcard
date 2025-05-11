import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FlashcardApp {
    private JFrame frame;
    private JLabel cardLabel;
    private JTextField searchField;
    private JButton prevButton, nextButton;
    private JLabel indexLabel;
    private boolean showingKey = true;

    private int currentIndex = 0;

    private ArrayList<Flashcard> cards = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlashcardApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("My Flashcards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("MY FLASHCARDS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(340, 20, 400, 40);
        frame.add(titleLabel);

        searchField = new JTextField();
        searchField.setBounds(100, 80, 700, 40);
        frame.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(820, 80, 120, 40);
        frame.add(searchButton);

        addSidebarButtons();

        cardLabel = new JLabel("", JLabel.CENTER);
        cardLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        cardLabel.setOpaque(true);
        cardLabel.setBackground(Color.WHITE);
        cardLabel.setBounds(300, 150, 700, 400);
        cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(cardLabel);

        cardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showingKey = !showingKey;
                updateCardDisplay();
            }
        });

        prevButton = new JButton("←");
        prevButton.setBounds(400, 580, 60, 40);
        frame.add(prevButton);

        nextButton = new JButton("→");
        nextButton.setBounds(700, 580, 60, 40);
        frame.add(nextButton);

        indexLabel = new JLabel("", JLabel.CENTER);
        indexLabel.setBounds(480, 580, 200, 40);
        indexLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        frame.add(indexLabel);

        prevButton.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                showingKey = true;
                updateCardDisplay();
            }
        });

        nextButton.addActionListener(e -> {
            if (currentIndex < cards.size() - 1) {
                currentIndex++;
                showingKey = true;
                updateCardDisplay();
            }
        });

        // Sample data
        cards.add(new Flashcard("Java", "A high-level programming language."));
        cards.add(new Flashcard("Swing", "Java GUI toolkit."));
        cards.add(new Flashcard("Polymorphism", "Ability to take many forms."));

        updateCardDisplay();

        frame.setVisible(true);
    }

    private void addSidebarButtons() {
        String[] labels = {"Add card", "Delete card", "Edit card", "Arrange cards", "Quiz"};
        Color[] colors = {Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW};

        for (int i = 0; i < labels.length; i++) {
            JButton button = new JButton(labels[i]);
            button.setBounds(50, 150 + i * 60, 200, 50);
            button.setBackground(colors[i]);
            frame.add(button);
        }
    }

    private void updateCardDisplay() {
        if (cards.isEmpty()) {
            cardLabel.setText("No cards available");
            indexLabel.setText("0/0");
            return;
        }

        Flashcard currentCard = cards.get(currentIndex);
        cardLabel.setText("<html><body style='width: 680px; text-align: center;'>" +
                (showingKey ? currentCard.key : currentCard.description) +
                "</body></html>");
        indexLabel.setText((currentIndex + 1) + "/" + cards.size());
    }

    static class Flashcard {
        String key;
        String description;

        Flashcard(String key, String description) {
            this.key = key;
            this.description = description;
        }
    }
}