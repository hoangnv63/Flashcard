import javax.swing.*;

public class FlashcardActions {
    public static void addCard(JFrame frame, FlashcardList list, FlashcardUI ui) {
        String key = JOptionPane.showInputDialog(frame, "Enter key:");
        if (key == null || key.trim().isEmpty()) return;

        String desc = JOptionPane.showInputDialog(frame, "Enter description:");
        if (desc == null || desc.trim().isEmpty()) return;

        list.add(new Flashcard(key, desc));

        if (ui.getCurrentNode() == null) {
            ui.setCurrentNode(list.getTail());  // first card
        }

        ui.updateCardDisplay();
    }

    public static void deleteCard(JFrame frame, FlashcardList list, FlashcardUI ui) {
        FlashcardNode current = ui.getCurrentNode();
        if (current == null) {
            JOptionPane.showMessageDialog(frame, "No card to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this card?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        FlashcardNode nextNode = current.next != null ? current.next : current.prev;
        list.delete(current);
        ui.setCurrentNode(nextNode);
        ui.updateCardDisplay();
    }
}