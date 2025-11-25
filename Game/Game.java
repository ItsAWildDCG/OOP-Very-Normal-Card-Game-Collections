package Game;

import java.awt.*;
import javax.swing.*;

/**
 * A placeholder class for the main Poker game screen.
 * This is where the table, cards, chips, and player UI would be placed.
 */
public class Game extends JFrame {

    // Theme colors
    private static final Color GAME_BG = new Color(0, 80, 0); // Dark Green felt
    private static final Color TEXT_COLOR = new Color(255, 255, 255);

    public Game() {
        // Frame
        setTitle("Very Normal Card Game - Playing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(GAME_BG);
        setLayout(new BorderLayout());

        // Placeholder
        JLabel statusLabel = new JLabel("This is a placeholder for the actual game.");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 36));
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Placeholder info
        JTextArea instructions = new JTextArea("This is just a placeholder, replace with actual game here.\nTo return just close this window and click run again in beans or whatever you use for java :skull:");
        instructions.setBackground(GAME_BG);
        instructions.setForeground(TEXT_COLOR.brighter());
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 16));
        instructions.setEditable(false);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setBackground(GAME_BG);
        centerPanel.add(statusLabel);
        centerPanel.add(instructions);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        add(centerPanel, BorderLayout.CENTER);
    }
}
