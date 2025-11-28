package Saves;

import Cards.Card;
import Decks.Deck;
import Items.Item;
import Person.*;

import java.util.ArrayList;
import java.util.List;

public class SaveData {
    public ArrayList<Item> items = new ArrayList<>();
    public Person community = new Person("Community", 1, 1, 1);
    public List<Card> discardpile = new ArrayList<>();
    public Deck dck = new Deck(false);
    public int discards = 2;
    public int round;
    public int turn;
    public Person[] gauntlet;
    public Person current;
    public Player you;
    public String state = "";
}
