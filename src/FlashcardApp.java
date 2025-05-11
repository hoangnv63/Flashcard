import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FlashcardApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlashcardUI ui = new FlashcardUI();
            ui.initUI();
        });
    }
}