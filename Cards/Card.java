package Cards;

public class Card {
    private String name, suit;
    private boolean faceUp;
    private int rank;
    private static final String[] Rank_names = {
        "", "Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
        "Nine", "Ten", "Jack", "Queen", "King"
    };
    public Card(int rank, String suit){
        setRank(rank);
        setSuit(suit);
    }
    
    public int getRank(){return rank;}

    public void setRank(int newRank){
        this.rank = newRank;
        updateName();
    }

    public String getSuit(){return suit;}

    public void setSuit(String newSuit){
        this.suit = newSuit;
        updateName();
    }

    public void increaseRank(){
        this.rank += 1;
    }

    public void decreaseRank(){
        this.rank -= 1;
    }

    public void updateName(){name = Rank_names[rank] + " of " + suit;}
    
    public string getName(){return name;}
}


