package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import data_structure.Trie;
import model.Flashcard;
import model.LinkedNode;
import data_structure.FlashcardLinkedList;

public class FlashcardActions {
    private static JDialog createDialog(JFrame parent, String title) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setSize(400, 500);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);
        return dialog;
    }

    private static JLabel createLabel(String text, int fontSize, int x, int y, int width, int height) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        label.setBounds(x, y, width, height);
        return label;
    }

    private static JScrollPane createTextArea(JTextArea area, int x, int y, int width, int height) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(x, y, width, height);
        return scroll;
    }

    private static JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        return button;
    }

    public static void addCard(JFrame parentFrame, FlashcardLinkedList list, FlashcardUI ui) {
        JDialog dialog = createDialog(parentFrame, "Add a new card");

        dialog.add(createLabel("Add a new card", 24, 50, 20, 300, 40));
        dialog.add(createLabel("Key", 16, 20, 80, 100, 30));
        JTextArea keyField = new JTextArea();
        dialog.add(createTextArea(keyField, 50, 110, 300, 80));

        dialog.add(createLabel("Description", 16, 20, 200, 150, 30));
        JTextArea descField = new JTextArea();
        dialog.add(createTextArea(descField, 50, 230, 300, 120));

        JButton addButton = createButton("Add", 150, 370, 100, 40);
        dialog.add(addButton);

        addButton.addActionListener(e -> {
            String key = keyField.getText().trim();
            String desc = descField.getText().trim();
            if (key.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in both fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            list.add(new Flashcard(key, desc));
            if (ui.getCurrentNode() == null) {
                ui.setCurrentNode(list.getTail());
            }
            ui.updateCardDisplay();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    public static void deleteCard(JFrame frame, FlashcardLinkedList list, FlashcardUI ui) {
        LinkedNode current = ui.getCurrentNode();
        if (current == null) {
            JOptionPane.showMessageDialog(frame, "No card to delete.", "Delete Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this card?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            LinkedNode nextNode = current.next != null ? current.next : current.prev;
            list.delete(current);
            ui.setCurrentNode(nextNode);
            ui.updateCardDisplay();
        }
    }

    public static void editCard(JFrame parentFrame, FlashcardLinkedList list, FlashcardUI ui) {
        LinkedNode current = ui.getCurrentNode();
        if (current == null) {
            JOptionPane.showMessageDialog(parentFrame, "No card to edit.", "Edit Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = createDialog(parentFrame, "Edit Card");

        dialog.add(createLabel("Edit Card", 24, 50, 20, 300, 40));
        dialog.add(createLabel("Key", 16, 20, 80, 100, 30));
        JTextArea keyField = new JTextArea(current.data.getKey());
        dialog.add(createTextArea(keyField, 50, 110, 300, 80));

        dialog.add(createLabel("Description", 16, 20, 200, 150, 30));
        JTextArea descField = new JTextArea(current.data.getDescription());
        dialog.add(createTextArea(descField, 50, 230, 300, 120));

        JButton saveButton = createButton("Save", 150, 370, 100, 40);
        dialog.add(saveButton);

        saveButton.addActionListener(e -> {
            String newKey = keyField.getText().trim();
            String newDesc = descField.getText().trim();
            if (newKey.isEmpty() || newDesc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in both fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            current.data.setKey(newKey);
            current.data.setDescription(newDesc);
            ui.updateCardDisplay();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    public static void sortCards(JFrame frame, FlashcardLinkedList list, FlashcardUI ui) {
        String[] options = {"Sort by Key", "Sort by Description"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Choose sorting method:",
                "Sort Flashcards",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            list.mergeSortByKey();
            JOptionPane.showMessageDialog(frame, "Flashcards sorted by Key.");
        } else if (choice == 1) {
            JOptionPane.showMessageDialog(frame, "Flashcards sorted by Description.");
        }

        ui.setCurrentNode(list.getHead());
        ui.updateCardDisplay();
    }

    public static void searchCard(JFrame parentFrame, FlashcardLinkedList list, Trie trie, FlashcardUI ui, String prefix) {
        if (prefix.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Please enter a prefix to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> suggestions = trie.suggest(prefix);
        if (suggestions.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No matching flashcards found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else if (suggestions.size() == 1) {
                String firstResult = suggestions.get(0);
                String key = firstResult.split(":", 2)[0].trim();
                LinkedNode node = list.findByKey(key);
                if (node != null) {
                    ui.setCurrentNode(node);
                    ui.updateCardDisplay();
                }
            } else {
                JList<String> listne = new JList<>(suggestions.stream().map(s -> s.split(":", 2)[0].trim()).toArray(String[]::new));
                listne.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listne.setVisibleRowCount(Math.min(20, suggestions.size()));

                JScrollPane scrollPane = new JScrollPane(listne);
                scrollPane.setPreferredSize(new Dimension(350, 150));

                int result = JOptionPane.showConfirmDialog(
                        ui.getFrame(),
                        scrollPane,
                        "Select a flashcard to view",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION && listne.getSelectedValue() != null) {
                    String selected = listne.getSelectedValue();
                    String key = selected.split(":", 2)[0].trim();
                    LinkedNode node = list.findByKey(key);
                    if (node != null) {
                        ui.setCurrentNode(node);
                        ui.updateCardDisplay();
                    }
                }
            }
        }
}
