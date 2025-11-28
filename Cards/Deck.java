package Cards;

import java.util.*;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();

    public Deck(){
        for(String s : Card.Suit_names){
            for(int r : Card.Ranks){
                if (r < 2) continue;
                Random rdm = new Random();
                if (rdm.nextInt(20) == 0) cards.add(new Card(r, s, true));
                else cards.add(new Card(r, s));
            }
        }
    }

    public Deck(boolean restart){
        return;
    }

    public ArrayList<Card> check(){
        return cards;
    }

    public void add_card(Card card){
        cards.add(card);
    }

    public Card peek(){
        if (!isEmpty()){
            return cards.getFirst();
        }
        else return null;
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public void removeCard(Card card){
        cards.remove(card);
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }
}
