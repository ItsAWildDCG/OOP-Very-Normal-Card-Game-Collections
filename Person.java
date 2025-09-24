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
        this.hand.add(i.peek());
        i.removeCard(i.peek());
    }
    void drawFrom(Deck i, int count){
        for (int n = 0; n< count; n++){
            this.hand.add(i.peek());
            i.removeCard(i.peek());
        }
    }
}
