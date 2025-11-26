package Game;

import Cards.Card;
import java.awt.*;
import javax.swing.*;

public class GameUI extends JFrame {

    // Colors
    private static final Color PRIMARY_BG = new Color(30, 30, 30);
    private static final Color BUTTON_BG = new Color(178, 34, 34);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color GAME_BG = new Color(0, 80, 0);

    // CardLayout manager
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public enum GameState {
        NORMAL,
        DISCARDING
    }

    public GameUI() {
        setTitle("Very Normal Card Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main container using CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add all screens
        mainPanel.add(createMenuPanel(), "menu");
        mainPanel.add(createGamePanel(), "game");
        mainPanel.add(createOptionsPanel(), "options");

        add(mainPanel);
    }

    /*
     * ========================
     * MENU SCREEN PANEL
     * ========================
     */
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);

        JLabel titleLabel = new JLabel("Very Normal Card Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 40, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        buttonPanel.setBackground(PRIMARY_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 300, 40, 300));

        JButton startGame = createButton("START NEW GAME", e -> showScreen("game"));
        JButton options = createButton("OPTIONS", e -> showScreen("options"));
        JButton exit = createButton("EXIT GAME", e -> System.exit(0));

        addHoverPanel(startGame, "<b>Start Game</b><br>Click to play!");

        buttonPanel.add(startGame);
        buttonPanel.add(options);
        buttonPanel.add(exit);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    /*
     * ========================
     * GAME SCREEN PANEL
     * ========================
     */
    private JPanel createGamePanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(GAME_BG);

        JLabel gameText = new JLabel("Game Screen Placeholder", SwingConstants.CENTER);
        gameText.setFont(new Font("Arial", Font.BOLD, 40));
        gameText.setForeground(Color.WHITE);

        JButton backButton = new JButton("← BACK TO MENU");
        backButton.setFont(new Font("Arial", Font.BOLD, 22));
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> showScreen("menu"));

        panel.add(backButton, BorderLayout.NORTH);

        // --- CARD AREA ---
        JPanel cardPanel = new JPanel(null);
        cardPanel.setOpaque(false); // optional, matches your background
        cardPanel.setBounds(500, 500, 400, 200);

        CardUI card1 = new CardUI(new Card(1, "Diamonds", false, false));
        card1.setBounds(0, 0, 100, 150); // x, y, width, height
        cardPanel.add(card1);

        CardUI card2 = new CardUI(new Card(11, "Hearts", true, false));
        card2.setBounds(120, 0, 100, 150); // offset x for spacing
        cardPanel.add(card2);

        CardUI card3 = new CardUI(new Card(7, "Spades", true, false));
        card3.setBounds(240, 0, 100, 150);
        cardPanel.add(card3);

        panel.add(cardPanel, BorderLayout.CENTER);

        // --- ACTION BUTTONS ---
        JPanel actionPanel = new JPanel(null);
        actionPanel.setOpaque(false);
        actionPanel.setBounds(800, 100, 150, 300);

        JButton callButton = createButton("Call", e -> handleCall());
        callButton.setBounds(0, 0, 120, 40);
        actionPanel.add(callButton);

        JButton raiseButton = createButton("Raise", e -> handleRaise());
        raiseButton.setBounds(0, 60, 120, 40);
        actionPanel.add(raiseButton);


        JButton foldButton = createButton("Fold", e -> handleFold());
        foldButton.setBounds(0, 120, 120, 40);
        actionPanel.add(foldButton);

        actionPanel.add(callButton);
        actionPanel.add(raiseButton);
        actionPanel.add(foldButton);

        panel.add(actionPanel, BorderLayout.EAST);
        return panel;
    }


    // --- ACTION METHODS ---
    private void handleCall() {
        System.out.println("Player chose to CALL");
        // TODO: Implement your call logic
    }

    private void handleRaise() {
        System.out.println("Player chose to RAISE");
        // TODO: Implement your raise logic
    }

    private void handleFold() {
        System.out.println("Player chose to FOLD");
        // TODO: Implement your fold logic
    }

    /*
     * ========================
     * OPTIONS SCREEN PANEL
     * ========================
     */
    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));

        JLabel title = new JLabel("Options Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JTextArea text = new JTextArea(
                "Options Coming Soon...\n\n" +
                        "- Volume\n" +
                        "- Graphics\n" +
                        "- Controls\n");
        text.setFont(new Font("Monospaced", Font.PLAIN, 20));
        text.setEditable(false);
        text.setBackground(new Color(45, 45, 45));
        text.setForeground(Color.WHITE);

        JButton backButton = new JButton("← BACK TO MENU");
        backButton.setFont(new Font("Arial", Font.BOLD, 22));
        backButton.setBackground(Color.GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> showScreen("menu"));

        panel.add(title, BorderLayout.NORTH);
        panel.add(text, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    /*
     * ========================
     * HELPERS
     * ========================
     */
    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 26));
        button.setBackground(BUTTON_BG);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(TEXT_COLOR, 3));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BG.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BG);
            }
        });

        button.addActionListener(action);
        return button;
    }

    // Hover for button
    private void addHoverPanel(JButton button, String text) {

        JPanel glass = (JPanel) getGlassPane();
        glass.setVisible(true);
        glass.setLayout(null);

        JPanel hoverPanel = new JPanel();
        hoverPanel.setBackground(new Color(255, 255, 200));
        hoverPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        hoverPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        hoverPanel.setLayout(new BorderLayout());
        hoverPanel.setVisible(false);

        JLabel label = new JLabel("<html>" + text + "</html>");
        hoverPanel.add(label);

        // Auto size
        hoverPanel.setSize(hoverPanel.getPreferredSize());

        glass.add(hoverPanel);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {

                // Convert button position to glass pane coordinates
                Point p = SwingUtilities.convertPoint(button.getParent(),
                        button.getX(), button.getY(), glass);

                hoverPanel.setLocation(p.x + button.getWidth() + 10, p.y);
                hoverPanel.setVisible(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hoverPanel.setVisible(false);
            }
        });
    }

    private void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    /*
     * ========================
     * MAIN
     * ========================
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameUI().setVisible(true);
        });
    }
}
