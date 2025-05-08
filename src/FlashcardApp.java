import javax.swing.*;
import java.awt.*;

public class FlashcardApp extends JFrame {

    private DefaultListModel<String> flashcardListModel = new DefaultListModel<>();
    private JList<String> flashcardList = new JList<>(flashcardListModel);
    private JTextField searchField = new JTextField(20);

    public FlashcardApp() {
        setTitle("Flashcard App");
        setSize(1080, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //panels
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        //buttons
        JButton addButton = new JButton("Add Card");
        JButton deleteButton = new JButton("Delete Card");
        JButton viewButton = new JButton("View Knowledge");
        JButton searchButton = new JButton("Search");

        //addListener
        addButton.addActionListener(e -> addCard());
        deleteButton.addActionListener(e -> deleteCard());
        viewButton.addActionListener(e -> viewKnowledge());
        searchButton.addActionListener(e -> searchCard());

        //div
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        centerPanel.add(new JScrollPane(flashcardList), BorderLayout.CENTER);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addCard() {
        String card = JOptionPane.showInputDialog(this, "Enter flashcard content:");
        if (card != null && !card.trim().isEmpty()) {
            flashcardListModel.addElement(card);
        }
    }

    private void deleteCard() {
        int index = flashcardList.getSelectedIndex();
        if (index >= 0) {
            flashcardListModel.remove(index);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a card to delete.");
        }
    }

    private void viewKnowledge() {
        if (flashcardListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No flashcards to show.");
        } else {
            StringBuilder allCards = new StringBuilder();
            for (int i = 0; i < flashcardListModel.size(); i++) {
                allCards.append(i + 1).append(". ").append(flashcardListModel.get(i)).append("\n");
            }
            JOptionPane.showMessageDialog(this, allCards.toString(), "Knowledge", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchCard() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a keyword to search.");
            return;
        }

        for (int i = 0; i < flashcardListModel.size(); i++) {
            String item = flashcardListModel.get(i).toLowerCase();
            if (item.contains(keyword)) {
                flashcardList.setSelectedIndex(i);
                flashcardList.ensureIndexIsVisible(i);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "No matching flashcard found.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlashcardApp().setVisible(true));
    }
}
