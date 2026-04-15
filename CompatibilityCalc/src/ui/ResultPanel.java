package ui;

import db.ResultDAO;
import engine.CompatibilityResult;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ResultPanel.java
 * Displays the final compatibility score, breakdown, and personality codes.
 */
public class ResultPanel extends JPanel {

    private MainFrame          frame;
    private CompatibilityResult result;

    private static final String[] TRAIT_LABELS = {
            "Social Energy", "Perceiving Style", "Empathy",
            "Creativity", "Ambition", "Adventurousness",
            "Conflict Style", "Learning Style", "Decision-Making",
            "Schedule Preference"
    };

    public ResultPanel(MainFrame frame, CompatibilityResult result) {
        this.frame  = frame;
        this.result = result;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        buildUI();
    }

    private void buildUI() {
        // ── Top — score and label ──────────────────────────────────
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        String names = result.getPerson1().getName() +
                " & " + result.getPerson2().getName();
        JLabel namesLabel = new JLabel(names, SwingConstants.CENTER);
        namesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        namesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(namesLabel);
        topPanel.add(Box.createVerticalStrut(8));

        JLabel scoreLabel = new JLabel(result.getScore() + " / 100",
                SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 36));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(scoreLabel);

        JLabel labelText = new JLabel(result.getLabel(), SwingConstants.CENTER);
        labelText.setFont(new Font("Arial", Font.ITALIC, 16));
        labelText.setForeground(new Color(30, 100, 180));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(labelText);
        topPanel.add(Box.createVerticalStrut(6));

        JLabel insightLabel = new JLabel(
                "<html><div style='text-align:center;width:400px'>"
                        + result.getInsight() + "</div></html>",
                SwingConstants.CENTER);
        insightLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        insightLabel.setForeground(Color.DARK_GRAY);
        insightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(insightLabel);

        add(topPanel, BorderLayout.NORTH);

        // ── Centre — trait breakdown + type codes ──────────────────
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));

        JLabel breakdownTitle = new JLabel("Dimension Breakdown");
        breakdownTitle.setFont(new Font("Arial", Font.BOLD, 13));
        breakdownTitle.setForeground(Color.GRAY);
        centrePanel.add(breakdownTitle);
        centrePanel.add(Box.createVerticalStrut(8));

        int[] scores = result.getDimensionScores();
        for (int i = 0; i < TRAIT_LABELS.length; i++) {
            centrePanel.add(buildTraitRow(TRAIT_LABELS[i],
                    i < scores.length ? scores[i] : 0));
        }

        centrePanel.add(Box.createVerticalStrut(12));

        // Personality type codes
        JPanel typePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        typePanel.add(buildTypeCard(result.getPerson1().getName(),
                result.getPerson1().getPersonalityCode()));
        typePanel.add(buildTypeCard(result.getPerson2().getName(),
                result.getPerson2().getPersonalityCode()));
        centrePanel.add(typePanel);

        JScrollPane scroll = new JScrollPane(centrePanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // ── Bottom — buttons ───────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));

        JButton top5Btn = new JButton("Top 5 Pairs");
        top5Btn.addActionListener(e -> showTopFive());
        btnPanel.add(top5Btn);

        JButton restartBtn = new JButton("Start Over");
        restartBtn.setFont(new Font("Arial", Font.BOLD, 13));
        restartBtn.addActionListener(e -> frame.showPanel(MainFrame.NAME_PANEL));
        btnPanel.add(restartBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    // Builds one trait row with a label and a progress bar
    private JPanel buildTraitRow(String label, int score) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        nameLabel.setPreferredSize(new Dimension(160, 20));
        row.add(nameLabel, BorderLayout.WEST);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(score);
        bar.setStringPainted(true);
        bar.setString(score + "%");
        row.add(bar, BorderLayout.CENTER);

        return row;
    }

    // Builds a small card showing a person's type code
    private JPanel buildTypeCard(String name, String code) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        nameLabel.setForeground(Color.GRAY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);

        JLabel codeLabel = new JLabel(code);
        codeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(codeLabel);

        return card;
    }

    // Shows top 5 compatible pairs in a dialog
    private void showTopFive() {
        List<String> pairs = ResultDAO.getTopFivePairs();
        if (pairs.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No results saved yet.",
                    "Top 5 Pairs", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("<html><body>");
        for (int i = 0; i < pairs.size(); i++) {
            sb.append("<p style='margin:4px 0'>")
                    .append(i + 1).append(".  ")
                    .append(pairs.get(i))
                    .append("</p>");
        }
        sb.append("</body></html>");

        JOptionPane.showMessageDialog(this,
                new JLabel(sb.toString()),
                "Top 5 Most Compatible Pairs",
                JOptionPane.PLAIN_MESSAGE);
    }
}
