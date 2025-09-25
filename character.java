public class character {
    private String characterName;
    private ArrayList<Card> hand;
    private LinkedHashMap<String, Integer> items = new LinkedHashMap<>();

    public character(String characterName){
        this.characterName = characterName;
        hand = new ArrayList<>();
    }
    public void useItem(String item){
        use(item);
        if (items.get(item) == 1){
            items.remove(item);
        }
        else{
            items.put(item, items.get(item) - 1);
        }
    }
    
    public void drawFrom(int x){
        hand.add(Deck.cards.get(x));
        Deck.cards.remove(x);
    }
}
