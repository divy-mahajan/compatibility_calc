package ui;

import javax.swing.*;
import java.awt.*;

/**
 * NamePanel.java
 * First screen — collects names of both participants before the quiz starts.
 */
public class NamePanel extends JPanel {

    private JTextField name1Field;
    private JTextField name2Field;
    private MainFrame  frame;

    public NamePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Compatibility Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Enter both names to begin", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        add(subtitle, gbc);

        // Person 1 label and field
        gbc.gridwidth = 1; gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Person 1:"), gbc);
        name1Field = new JTextField(20);
        gbc.gridx = 1;
        add(name1Field, gbc);

        // Person 2 label and field
        gbc.gridy = 3; gbc.gridx = 0;
        add(new JLabel("Person 2:"), gbc);
        name2Field = new JTextField(20);
        gbc.gridx = 1;
        add(name2Field, gbc);

        // Start button
        JButton startBtn = new JButton("Start →");
        startBtn.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        add(startBtn, gbc);

        startBtn.addActionListener(e -> handleStart());
    }

    private void handleStart() {
        String name1 = name1Field.getText().trim();
        String name2 = name2Field.getText().trim();

        if (name1.isEmpty() || name2.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both names before continuing.",
                    "Names required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (name1.length() > 50 || name2.length() > 50) {
            JOptionPane.showMessageDialog(this,
                    "Names must be 50 characters or fewer.",
                    "Name too long",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Launch question flow for person 1
        QuestionPanel questionPanel = new QuestionPanel(frame, name1, name2);
        frame.addAndShow(questionPanel, MainFrame.QUESTION_PANEL);
    }
}
