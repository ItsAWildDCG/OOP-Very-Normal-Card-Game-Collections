package Cards;

public class Card {
    private String name, suit;
    private boolean faceUp, wild;
    private int rank;
    public static final int[] Ranks = {
            0, 1 , 2, 3, 4, 5, 6, 7, 8, 9 , 10 , 11, 12 , 13 , 14
    };

    public static final String[] Rank_names = {
            "", "Aces", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
            "Nine", "Ten", "Jack", "Queen", "King", "Aces"
    };

    public static final String[] Suit_names = {
            "Spades", "Hearts", "Diamonds",  "Clubs"
    };

    public Card(int rank, String suit){
        setRank(rank);
        setSuit(suit);
        setWild(false);
        updateName();
    }

    public Card(int rank, String suit, boolean wild){
        setRank(rank);
        setSuit(suit);
        setWild(wild);
        updateName();
    }

    public int getRank(){return rank;}

    public void setRank(int newRank){
        this.rank = newRank;
        updateName();
    }

    public void setWild(boolean wild) {
        this.wild = wild;
    }

    public String getSuit(){return suit;}

    public void setSuit(String newSuit){
        this.suit = newSuit;
        updateName();
    }

    public boolean isWild() {
        return wild;
    }

    public void changeRank(int r){
        this.rank += r;
    }


    public void updateName(){
        if (wild) name = "Wild ";
        else name = "";
        name += Rank_names[rank] + " of " + suit;
    }

    public String toString(){return name;}
}