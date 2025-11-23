package Decks;

import Cards.*;
import java.util.*;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();
    private String type;

    public Deck(){
        for(String s : Card.Suit_names){
            for(int r : Card.Ranks){
                if (r < 2) continue;
                cards.add(new Card(r, s));
            }
        }
    }

    public ArrayList<Card> check(){
        return cards;
    }

    public void add_card(Card card){
        cards.add(card);
    }

    public Card peek(){
        if (!isEmpty()){
            return cards.get(0);
        }
        else return null;
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public void removeCard(Card card){
        if (cards.contains(card)){
            cards.remove(card);
        }
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }
}
