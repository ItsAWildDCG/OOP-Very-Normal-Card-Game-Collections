package Person;

import java.util.*;
import java.math.*;
import Decks.Deck;
import Items.*;

public class Person {
    private String CharName;
    private int init_card_draw = 2;
    private int baseChips, currentChips;
    private List<Card> hand = new ArrayList<>();
    private Map<Item, Integer> items = new LinkedHashMap<>();
    
    public Person(){
        this.CharName = "Jimbo";
    }
    public Person(String s){
        this.CharName = s;
    }

    public int getBaseChips(){
        return baseChips;
    }

    public int getCurrentChips(){
        return currentChips;
    }
    
    void viewHand(){
        if (this.hand.isEmpty()){
            System.out.println(this.CharName + "'s hand is empty");
        }
        else{
            System.out.print(this.CharName + "'s hand: ");
            for (Card i: hand){
                if(i.checkFlip()){
                    System.out.print(i.displayCard() + " ");
                }
                else{
                    i.flip();
                    System.out.print(i.displayCard() + " ");
                    i.flip();
                }
            }
            System.out.println();
        }

    }
    
    void drawFrom(Deck i){
        if(i.isEmpty()){
            System.out.println("Uh uh uh, ya can't draw from an empty deck ya know");
        }
        else{
            this.hand.add(i.peek());
            i.removeCard(i.peek());
        }
    }
    
    void drawXFrom(Deck i, int X){
        for (int n = 0; n< X; n++){
            drawFrom(i);
        }
    }

    void addItem(Item z){
        if(items.containsKey(z)){
            items.put(z, items.get(z) + 1);
        }
        else{
            items.put(z, 1);
        }
    }

    void decreaseItem(Item z){
        if (items.get(z) == null) return;
        if(items.get(z) == 1){
            items.remove(z);
        else{
            items.put(z, items.get(z) - 1);
        }
    }

    void useItemOnSelf(Item z){
        if(items.containsKey(z)){
            z.use(this);
            decreaseItem(z);
        }
        else{
            System.out.println("What are you trying to do");
        }
    }

    void useItemOnDeck(Item z, Deck y){
        if(items.containsKey(z)){
            z.use(this, y);
            decreaseItem(z);
        }
        else{
            System.out.println("What are you trying to do");
        }
    }

    void useItemOnTarget(Item z, Person target){
        if(items.containsKey(z)){
            z.use(this, target);
            decreaseItem(z);
        }
        else{
            System.out.println("What are you trying to do");
        }
    }


    void increaseCardDraw(int a){
        init_card_draw += a;
    }

    void decreaseCardDraw(int a){
        init_card_draw -= a;
    }
    
    void healed(int a){
        currentChips += Math.min(a, baseChips - currentChips);
    }

    void takeDamage(int a){
        currentChips -= a;
    }
    
}


















