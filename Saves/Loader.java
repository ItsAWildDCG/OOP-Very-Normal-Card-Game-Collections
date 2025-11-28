package Saves;

import Cards.Card;
import Decks.Deck;
import Items.Item;
import Person.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static Items.Item.itemGen;

public class Loader {
    public static SaveData load(Scanner inp){
        try (Scanner savefile = new Scanner (new File("Saves/game.sav"))){
            SaveData data = new SaveData();
            if (!savefile.hasNextInt()) throw new Exception();
            System.out.println("A saved game has been found. Would you like to continue? (y/n)");
            while(true){
                String play = inp.nextLine();
                if(play.equals("n")) return null;
                if(play.equals("y")) break;
            }
            data.discards = savefile.nextInt();
            data.round = savefile.nextInt();
            data.turn = savefile.nextInt();
            data.gauntlet = new Person[savefile.nextInt()];
            savefile.nextLine();
            String nameboss;
            for (int i = 0; i< data.gauntlet.length; i++) {
                nameboss = savefile.nextLine();
                switch(nameboss){
                    case "Joe":
                        data.gauntlet[i] = new Joe(nameboss, savefile.nextInt());
                        break;
                    case "Breno":
                        data.gauntlet[i] = new Breno(nameboss, savefile.nextInt(), data.turn-1);
                        break;
                    default:
                        data.gauntlet[i] = new Person(nameboss, savefile.nextInt());
                }
                savefile.nextLine();
            }
            data.current = data.gauntlet[data.round-1];
            data.current.changeChips(savefile.nextInt()-data.current.getBaseChips());
            int currentsize = savefile.nextInt();
            savefile.nextLine();
            while(currentsize-->0) data.current.getHand().add(new Card(savefile.nextLine()));
            int communitysize = savefile.nextInt();
            savefile.nextLine();
            while(communitysize-->0) data.community.getHand().add(new Card(savefile.nextLine()));
            data.you = new Player(savefile.nextLine(), savefile.nextDouble(), savefile.nextDouble(), savefile.nextInt(), savefile.nextInt());
            data.you.changeVampbuff(savefile.nextInt());
            data.you.changeBlindbuff(savefile.nextInt());
            savefile.nextLine();
            int itemsize = savefile.nextInt();
            savefile.nextLine();
            while(itemsize-->0) itemGen(data.items, savefile.nextLine());
            int playersize = savefile.nextInt();
            savefile.nextLine();
            while(playersize-->0) data.you.getHand().add(new Card(savefile.nextLine()));
            int discardsize = savefile.nextInt();
            savefile.nextLine();
            while(discardsize-->0) data.discardpile.add(new Card(savefile.nextLine()));
            while(savefile.hasNextLine()) data.dck.add_card(new Card(savefile.nextLine()));
            System.out.println("Load successful!");
            data.state = "Continue";
            return data;
        }
        catch (Exception e){
            System.out.println("Save data unavailable or corrupted. Starting a new game.");
            return null;
        }
    }
}
