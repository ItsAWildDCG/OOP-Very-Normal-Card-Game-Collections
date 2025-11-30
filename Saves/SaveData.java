package Saves;

import Cards.*;
import Items.Item;
import Person.*;
import java.util.ArrayList;
import java.util.List;

public class SaveData {
    public Item[] items = new Item[6];
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

    public SaveData() {
        return;
    }

    public SaveData(Item[] items, Person community, List<Card> discardpile, Deck dck, int discards, int round, int turn, Person[] gauntlet, Person current, Player you, String state) {
        this.items = items;
        this.community = community;
        this.discardpile = discardpile;
        this.dck = dck;
        this.discards = discards;
        this.round = round;
        this.turn = turn;
        this.gauntlet = gauntlet;
        this.current = current;
        this.you = you;
        this.state = state;
    }
}
