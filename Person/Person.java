package Person;

import java.util.*;
import java.math.*;

import Battle.TypeTable;
import Cards.Card;
import Decks.Deck;
import Items.*;

public class Person {
    private String CharName;
    private int init_card_draw = 2;
    private double atkMult, defMult;
    private int baseChips, currentChips;
    private List<Card> hand = new ArrayList<>();
    private TypeTable table = new TypeTable();
    public Person(String charName, int baseChips) {
        this.CharName = charName;
        this.baseChips = baseChips;
        currentChips = baseChips;
        atkMult = 1;
        defMult = 1;
    }

    public double getAtkMult() {
        return atkMult;
    }

    public double getDefMult() {
        return defMult;
    }

    public String getName() {
        return CharName;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getInit_card_draw() {
        return init_card_draw;
    }

    public Person(String charName, double atkMult, double defMult, int baseChips) {
        CharName = charName;
        this.atkMult = atkMult;
        this.defMult = defMult;
        this.baseChips = baseChips;
        currentChips = baseChips;
    }

    public int getBaseChips() {
        return baseChips;
    }

    public int getCurrentChips() {
        return currentChips;
    }

    public TypeTable getTable() {
        return table;
    }

    public void drawFrom(Deck i) {
        if (i.isEmpty()) {
            System.out.println("Uh uh uh, ya can't draw from an empty deck ya know");
        } else {
            this.hand.add(i.peek());
            i.removeCard(i.peek());
        }
    }

    public void drawXFrom(Deck i, int X) {
        for (int n = 0; n < X; n++) {
            drawFrom(i);
        }
    }

    public void discardTop() {
        hand.removeLast();
    }

    public void discard(Card c) {
        hand.remove(c);
    }

    public void changeCardDraw(int a) {
        init_card_draw += a;
    }

    public void changeChips(int a) {
        if (a>0) currentChips += Math.min(a, baseChips - currentChips);
        else currentChips = Math.max(currentChips+a, 0);
    }

}

