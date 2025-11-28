package Cards;

import java.util.List;

public class Card {
    private String name, suit;
    private boolean faceUp = true, wild, selected = false;
    private int rank;
    public static final int[] Ranks = {
            0, 1 , 2, 3, 4, 5, 6, 7, 8, 9 , 10 , 11, 12 , 13 , 14
    };

    public static final String[] Rank_names = {
            "", "", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
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

    public Card(int rank, String suit,boolean wild, boolean faceUp){
        setRank(rank);
        setSuit(suit);
        setWild(wild);
        setFaceUp(faceUp);
        updateName();
    }

    public Card(String s){
        String[] newcard = s.split("\\s+");
        if (newcard[0].equals("Wild")){
            setWild(true);
            rank = List.of(Rank_names).indexOf(newcard[1]);
            suit = newcard[3];
        }
        else{
            setWild(false);
            rank = List.of(Rank_names).indexOf(newcard[0]);
            suit = newcard[2];
        }
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

    public void selectCard(){
        selected = !selected;
    }

    public boolean isSelected(){
        return selected;
    }

    public boolean getFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean a){
        faceUp = a;
    }

    public boolean isWild() {
        return wild;
    }

    public void changeRank(int r){
        rank = (rank+r-2)%13+2;
    }

    public void updateName(){
        if (wild) name = "Wild ";
        else name = "";
        name += Rank_names[rank] + " of " + suit;
    }

    public String toString(){return name;}


}