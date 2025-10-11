package Person;

import java.util.*;
import Decks.Deck;
import Items.*;

public class Person {
    private String CharName;
    private int init_card_draw = 2;
    private int baseChips, currentChips;
    private List<Card> hand = new ArrayList<>();
    private Map<item, Integer> items = new LinkedHashMap<>();
    Person(){
        this.CharName = "Jimbo";
    }
    Person(String s){
        this.CharName = s;
    }
    void viewHand(){
        if (this.hand.isEmpty()){
            System.out.println(this.CharName + "'hand is empty");
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
            System.out.printl("Uh uh uh, ya can't draw from an empty deck ya know");
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

    void addItem(item z){
        if(items.containsKey(z)){
            items.put(z, items.get(z) + 1);
        }
        else{
            items.put(z, 1);
        }
    }

    void decreaseItem(item z){
        

    void useItem(item z){
        if(items.containsKey(z)){
            z.use();
            if(items.get(z) == 1){
                items.remove(z);
            else{
                items.put(z, items.get(z) - 1);
            }
        }
        else{
            System.out.printl("What are you trying to do");
        }
    }


    void increaseCardDraw(int a){
        init_card_draw += a;
    }

    void decreaseCardDraw(int a){
        init_card_draw -= a;
    }
    
    void increaseChips(int a){
        currentChips += a;
    }

    void decreaseChips(int a){
        currentChips -= a;
    }
    
}












