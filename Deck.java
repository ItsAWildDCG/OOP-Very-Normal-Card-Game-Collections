import java.util.*;

public class Deck {
    private List<Card> cards = new ArrayList<>();
    private List<String> suits = new ArrayList<>();
    private List<String> ranks = new ArrayList<>();
    Deck(){
        this.suits.add("Spades");
        this.suits.add("Hearts");
        this.suits.add("Clubs");
        this.suits.add("Diamonds");
        this.ranks.add("Two");
        this.ranks.add("Three");
        this.ranks.add("Four");
        this.ranks.add("Five");
        this.ranks.add("Six");
        this.ranks.add("Seven");
        this.ranks.add("Eight");
        this.ranks.add("Nine");
        this.ranks.add("Ten");
        this.ranks.add("Jack");
        this.ranks.add("Queen");
        this.ranks.add("King");
        this.ranks.add("Ace");
        for (String i: this.suits){
            for (String j: this.ranks){
                this.cards.add(new Card(j, i));
            }
        }
        Collections.shuffle(cards);
    }
    Deck(List<String> suits, List<String> ranks){
        for (String i: suits){
            for (String j: ranks){
                this.cards.add(new Card(j, i));
            }
        }
        Collections.shuffle(this.cards);
    }
    void showAllCards(){
        for (Card i: this.cards){
            System.out.println(i.displayCard());
        }
    }
    void removeCard(Card i){
        if (!this.cards.remove(i)){
            System.out.println("No cards were removed");
        }
    }
    void addCard(Card i){
        this.cards.add(i);
    }
    void shuffle(){
        Collections.shuffle(this.cards);
    }
    Card peek(){
        return this.cards.getFirst();
    }
}
