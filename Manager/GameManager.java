package Manager;

import Saves.Loader;
import Saves.SaveData;

import java.util.Random;
import java.util.Scanner;

import static Manager.ActionManager.*;

public class GameManager {
    private final Scanner inp = new Scanner(System.in);
    private final Random rdm = new Random();
    private SaveData data;
    public void run() {
        data = Loader.load(inp);
        if (data == null)
            data = GameInitializer.newGame();
        else
            startOfTurn(data);
        gameLoop();
        endGame();
    }
    private void gameLoop() {
        while (data.you.getCurrentChips() > 0){
            startTurn(data, inp, rdm);
            if (data.state.equals("Quit"))
                return;
            resolveTurn(data);
            if (data.round > data.gauntlet.length)
                return;
        }
    }
    private void endGame() {
        if (!data.state.equals("Quit"))
            finishGame(data);
    }
}
