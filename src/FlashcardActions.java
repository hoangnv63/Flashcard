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
}