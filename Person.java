import java.util.*;

public class Person {
    private String CharName;
    private List<Card> hand = new ArrayList<>();
    private Map<Integer, Integer> items = new TreeMap<>();
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
}

