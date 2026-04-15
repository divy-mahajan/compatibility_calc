package ui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * MainFrame.java
 * The main application window. Manages panel switching throughout the app.
 */
public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public static final String NAME_PANEL     = "NAME";
    public static final String QUESTION_PANEL = "QUESTION";
    public static final String RESULT_PANEL   = "RESULT";

    public MainFrame() {
        setTitle("Compatibility Calculator");
        setSize(700, 520);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); // Centre on screen
        setResizable(false);

        // Close DB connection cleanly when window is closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                DBConnection.closeConnection();
                System.exit(0);
            }
        });

        // CardLayout lets us switch between panels without opening new windows
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        NamePanel namePanel = new NamePanel(this);
        mainPanel.add(namePanel, NAME_PANEL);

        add(mainPanel);
        setVisible(true);

        // Start on the name entry screen
        showPanel(NAME_PANEL);
    }

    // Call this from any panel to switch screens
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    // Adds a panel dynamically — used when QuestionPanel and ResultPanel
    // are created mid-session with fresh state
    public void addAndShow(JPanel panel, String name) {
        mainPanel.add(panel, name);
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        // Run UI on the Event Dispatch Thread as Swing requires
        SwingUtilities.invokeLater(MainFrame::new);
    }
}