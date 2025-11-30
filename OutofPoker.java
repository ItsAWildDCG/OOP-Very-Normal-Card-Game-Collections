import Game.GameUI;
import javax.swing.SwingUtilities;
public class OutofPoker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameUI().setVisible(true));
    }
}
