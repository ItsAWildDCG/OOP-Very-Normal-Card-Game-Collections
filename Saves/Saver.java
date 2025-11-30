package Saves;

import Cards.Card;
import Items.Item;
import Person.Person;

import java.io.FileWriter;

public class Saver {
    public static void save(SaveData data) {
        try (FileWriter savefile = new FileWriter("Saves/game.sav", false)) {
            savefile.write(String.format("%d %d %d\n", data.discards, data.round, data.turn));
            savefile.write(data.gauntlet.length + "\n");
            for (Person p : data.gauntlet) {
                savefile.write(String.format(p.getName() + "\n"));
                savefile.write(String.format("%d\n", p.getBaseChips()));
            }
            savefile.write(data.current.getCurrentChips() + "\n");
            savefile.write(data.current.size() + "\n");
            for (Card c : data.current.getHand())
                savefile.write(c + "\n");
            savefile.write(data.community.size() + "\n");
            for (Card c : data.community.getHand())
                savefile.write(c + "\n");
            savefile.write(data.you.getName() + "\n");
            savefile.write(String.format("%.1f %.1f %d %d %d %d\n", data.you.getAtkMult(), data.you.getDefMult(),
                    data.you.getCurrentChips(), data.you.getBaseChips(), data.you.getVampbuff(),
                    data.you.getBlindbuff()));
            for (int i = 0; i < 6; i++) {
                if (data.items[i] == null)
                    savefile.write("null\n");
                else
                    savefile.write(data.items[i].getName() + "\n");
            }
            savefile.write(data.you.size() + "\n");
            for (Card c : data.you.getHand())
                savefile.write(c + "\n");
            savefile.write(data.discardpile.size() + "\n");
            for (Card c : data.discardpile)
                savefile.write(c + "\n");
            for (Card c : data.dck.check())
                savefile.write(c + "\n");
            System.out.println("Saved your progress! Now quitting.");
        } catch (Exception e) {
            System.out.println("Saving failed. Quitting without saving.");
        }
    }

    public static void deleteSave(){
        try (FileWriter savefile = new FileWriter("Saves/game.sav", false)) {
            savefile.write("NO DATA");
        } catch (Exception _) {}
    }
}
