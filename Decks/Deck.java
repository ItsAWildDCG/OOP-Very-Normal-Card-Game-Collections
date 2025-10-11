package Decks;

public class Deck {
    private ArrayList<Card> cards;
    private String type;

    public Deck(String type){
        this.type = type;
    }

    public ArrayList<Card> check(){
        return cards;
    }
    
    public void add_card(Card card){
        cards.add(card);
    }

    public Card peek(){
        if (!are_u_empty()){
            return cards.get(0);
        }
        else return null;
    }

    @Override
    protected boolean isEmpty(){
        return Cards.isEmpty();
    }
    
    public void remove_card(Card card){
        if cards.contains(card){
            cards.remove(card);
        }
    }
}





