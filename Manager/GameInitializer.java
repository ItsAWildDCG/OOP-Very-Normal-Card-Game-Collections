package Manager;
import Decks.Deck;
import Person.*;
import Saves.SaveData;
import java.util.ArrayList;

public class GameInitializer {
    public static SaveData newGame() {
        SaveData data = new SaveData();
        data.items = new ArrayList<>();
        data.gauntlet = new Person[4];
        data.community = new Person("community", 1);
        data.you = new Player("DCG", 1.2, 1.3, 100);
        data.gauntlet[0] = new Person("Fred", 1.0, 1.0, 150);
        data.gauntlet[1] = new Person("Biden", 1.1, 1.1, 200);
        data.gauntlet[2] = new Breno("Breno", 160);
        data.gauntlet[3] = new Joe("Joe", 250);
        data.round = 1;
        data.discards = 2;
        data.current = data.gauntlet[0];
        data.turn = 0;
        data.dck = new Deck();
        System.out.printf("****************ROUND %d****************\n\n", data.round);
        return data;
    }
}
