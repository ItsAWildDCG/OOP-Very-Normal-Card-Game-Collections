package Person;

import java.util.*;
import java.math.*;

import Battle.TypeTable;
import Cards.Card;
import Decks.Deck;
import Items.*;

public class Person {
    private String CharName, state;
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
        state = "";
    }

    public Person(String charName, double atkMult, double defMult, int baseChips) {
        CharName = charName;
        this.atkMult = atkMult;
        this.defMult = defMult;
        this.baseChips = baseChips;
        currentChips = baseChips;
        state = "";
    }

    public Person(String charName, double atkMult, double defMult, int currentChips, int baseChips) {
        CharName = charName;
        this.atkMult = atkMult;
        this.defMult = defMult;
        this.baseChips = baseChips;
        this.currentChips = currentChips;
        state = "";
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

    public int size(){
        return hand.size();
    }

    public int getInit_card_draw() {
        return init_card_draw;
    }

    public String getState() {
        return state;
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

    public boolean drawFrom(Deck i) {
        if (i.isEmpty()) {
            System.out.println("Uh uh uh, ya can't draw from an empty deck ya know");
            return false;
        } else {
            this.hand.add(i.peek());
            i.removeCard(i.peek());
            return true;
        }
    }

    public void drawXFrom(Deck i, int X) {
        for (int n = 0; n < X; n++) {
            if (!drawFrom(i)) break;
        }
    }

    public void changeAtk(double i){
        atkMult = Math.max(atkMult+i, 0);
    }

    public void changeDef(double i){
        defMult = Math.max(defMult+i, 0);
    }

    public Card discardTop() {
        return hand.removeLast();
    }

    public Card discard(Card c) {
        if (hand.contains(c)) {
            hand.remove(c);
            return c;
        }
        return null;
    }

    public void changeCardDraw(int a) {
        init_card_draw += a;
    }

    public void changeChips(int a) {
        if (a>0) currentChips += Math.min(a, baseChips - currentChips);
        else currentChips = Math.max(currentChips+a, 0);
    }

    public boolean isPlayer(){
        return false;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String toString(){
        StringBuilder stats = new StringBuilder("<");
        for (int i = 0; i<10; i++){
            if (currentChips>=baseChips*i/10) stats.append("[]");
            else stats.append("--");
        }
        stats.append(String.format("> %s %d/%d", CharName, currentChips, baseChips));
        return stats.toString();
    }

    public boolean hasNoCards(){
        return hand.isEmpty();
    }

    public void uniqueAction(){
        return;
    }

}

