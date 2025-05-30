package ui;

import model.Flashcard;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizDialog extends JDialog {
    private final List<Flashcard> questions;
    private int currentIndex = 0;
    private int score = 0;

    private final JTextArea questionArea;
    private final JTextField answerField;
    private final JTextArea feedbackArea;
    private final JButton checkButton;
    private final JButton nextButton;
    private final JLabel progressLabel;

    public QuizDialog(JFrame parent, List<Flashcard> allCards) {
        super(parent, "Flashcard Quiz", true);
        setSize(500, 320);
        setLocationRelativeTo(parent);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);


        Collections.shuffle(allCards);
        questions = new ArrayList<>(allCards.subList(0, Math.min(3, allCards.size())));

        questionArea = new JTextArea();
        questionArea.setFont(new Font("SansSerif", Font.BOLD, 17));
        questionArea.setBounds(40, 20, 400, 48);
        questionArea.setWrapStyleWord(true);
        questionArea.setLineWrap(true);
        questionArea.setOpaque(false);
        questionArea.setEditable(false);
        questionArea.setFocusable(false);
        questionArea.setBorder(null);
        questionArea.setHighlighter(null);
        add(questionArea);

        answerField = new JTextField();
        answerField.setBounds(40, 80, 400, 35);
        add(answerField);

        checkButton = new JButton("Check");
        checkButton.setBounds(100, 130, 100, 35);
        add(checkButton);

        nextButton = new JButton("Next");
        nextButton.setBounds(280, 130, 100, 35);
        nextButton.setEnabled(false);
        add(nextButton);

        feedbackArea = new JTextArea();
        feedbackArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        feedbackArea.setBounds(40, 170, 400, 38); // tăng chiều cao nếu muốn wrap nhiều dòng
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setLineWrap(true);
        feedbackArea.setOpaque(false);
        feedbackArea.setEditable(false);
        feedbackArea.setFocusable(false);
        feedbackArea.setBorder(null);
        feedbackArea.setHighlighter(null);
        feedbackArea.setForeground(Color.BLACK);
        add(feedbackArea);

        progressLabel = new JLabel("", JLabel.CENTER);
        progressLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        progressLabel.setBounds(40, 220, 400, 30);
        add(progressLabel);

        checkButton.addActionListener(_ -> checkAnswer());
        nextButton.addActionListener(_ -> nextQuestion());

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentIndex >= questions.size()) {
            questionArea.setText("Quiz Finished!");
            answerField.setVisible(false);
            checkButton.setVisible(false);
            nextButton.setVisible(false);
            feedbackArea.setText("Your score: " + score + "/" + questions.size());
            progressLabel.setText("");
            return;
        }
        Flashcard card = questions.get(currentIndex);
        questionArea.setText("What is the key of: " + card.getDescription() + "?");
        answerField.setText("");
        answerField.setEditable(true);
        feedbackArea.setText("");
        feedbackArea.setForeground(Color.BLACK);
        checkButton.setEnabled(true);
        nextButton.setEnabled(false);
        progressLabel.setText("Question " + (currentIndex + 1) + " of " + questions.size());
    }

    private void checkAnswer() {
        Flashcard card = questions.get(currentIndex);
        String userAnswer = answerField.getText().trim().toLowerCase();
        String correctAnswer = card.getKey().trim().toLowerCase();

        if (userAnswer.equals(correctAnswer)) {
            feedbackArea.setText("✅ Correct!");
            feedbackArea.setForeground(new Color(0, 130, 0));
        } else {
            feedbackArea.setText("❌ Incorrect!\nAnswer: " + card.getKey());
            feedbackArea.setForeground(Color.RED);
        }

        answerField.setEditable(false);
        checkButton.setEnabled(false);
        nextButton.setEnabled(true);
    }

    private void nextQuestion() {
        currentIndex++;
        loadQuestion();
    }
}

