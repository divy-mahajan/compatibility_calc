package ui;

import engine.QuestionBank;
import engine.Question;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QuestionPanel.java
 * Cycles through all 10 questions for each person in turn.
 * Stores answers in a Map<String, Integer> per person.
 */
public class QuestionPanel extends JPanel {

    private MainFrame            frame;
    private String               name1, name2;
    private List<Question>       questions;
    private int                  currentQuestion = 0;
    private int                  currentPerson   = 0; // 0 = person1, 1 = person2
    private Map<String, Integer> answers1        = new HashMap<>();
    private Map<String, Integer> answers2        = new HashMap<>();

    private JLabel        personLabel;
    private JLabel        progressLabel;
    private JLabel        questionLabel;
    private ButtonGroup   optionGroup;
    private JRadioButton[] optionButtons;
    private JButton        nextBtn;

    public QuestionPanel(MainFrame frame, String name1, String name2) {
        this.frame  = frame;
        this.name1  = name1;
        this.name2  = name2;
        this.questions = new QuestionBank().getRandomQuestions(10);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        buildUI();
        loadQuestion();
    }

    private void buildUI() {
        // Top — person name and progress
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        personLabel   = new JLabel("", SwingConstants.CENTER);
        personLabel.setFont(new Font("Arial", Font.BOLD, 16));
        progressLabel = new JLabel("", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        progressLabel.setForeground(Color.GRAY);
        topPanel.add(personLabel);
        topPanel.add(progressLabel);
        add(topPanel, BorderLayout.NORTH);

        // Centre — question text and options
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel("<html><body style='width:400px'></body></html>");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 15));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        centrePanel.add(questionLabel);

        optionGroup   = new ButtonGroup();
        optionButtons = new JRadioButton[3];
        for (int i = 0; i < 3; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 13));
            optionButtons[i].setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
            optionGroup.add(optionButtons[i]);
            centrePanel.add(optionButtons[i]);
            // Enable Next button only after an option is selected
            optionButtons[i].addActionListener(e -> nextBtn.setEnabled(true));
        }
        add(centrePanel, BorderLayout.CENTER);

        // Bottom — Next button
        nextBtn = new JButton("Next →");
        nextBtn.setFont(new Font("Arial", Font.BOLD, 14));
        nextBtn.setEnabled(false); // Disabled until option selected
        nextBtn.addActionListener(e -> handleNext());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(nextBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadQuestion() {
        Question q    = questions.get(currentQuestion);
        String   name = currentPerson == 0 ? name1 : name2;

        personLabel.setText(name + "'s questions");
        progressLabel.setText("Question " + (currentQuestion + 1) + " of " + questions.size());
        questionLabel.setText("<html><body style='width:400px'>" + q.getText() + "</body></html>");

        String[] options = q.getOptions();
        for (int i = 0; i < 3; i++) {
            optionButtons[i].setText(options[i]);
            optionButtons[i].setSelected(false);
        }

        optionGroup.clearSelection();
        nextBtn.setEnabled(false);
        nextBtn.setText(isLastQuestion() ? "See results →" : "Next →");
    }

    private void handleNext() {

        Question q = questions.get(currentQuestion);

        for (int i = 0; i < 3; i++) {
            if (optionButtons[i].isSelected()) {

                int value = q.getValueForOption(i);

                if (currentPerson == 0) {
                    answers1.merge(q.getDimension(), value, Integer::sum);
                } else {
                    answers2.merge(q.getDimension(), value, Integer::sum);
                }
                break;
            }
        }

        if (isLastQuestion()) {
            if (currentPerson == 0) {
                currentPerson = 1;
                currentQuestion = 0;
                loadQuestion();
            } else {
                ResultController controller = new ResultController(
                        frame, name1, name2, answers1, answers2
                );
                controller.run();
            }
        } else {
            currentQuestion++;
            loadQuestion();
        }
    }

    private boolean isLastQuestion() {
        return currentQuestion == questions.size() - 1;
    }
}
