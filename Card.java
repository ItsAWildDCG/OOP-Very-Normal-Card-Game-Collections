import java.util.*;

public class Card {
    private String rank, suit;
    private boolean face_up;
    Card(){
        this.rank = "Ace";
        this.suit = "Spade";
        this.face_up = true;
    }
    Card(String rank, String suit){
        this.rank = rank;
        this.suit = suit;
        this.face_up = true;
    }
    String getRank(){
        return this.rank;
    }
    String getSuit(){
        return this.suit;
    }
    void setRank(String rank){
        this.rank = rank;
    }
    void setSuit(String suit){
        this.suit = suit;
    }
    void flip(){
        this.face_up = !face_up;
    }
    boolean checkFlip(){
        return this.face_up;
    }
    String displayCard(){
        StringBuilder output = new StringBuilder();
        output.append("[");
        if (face_up){
            output.append(this.rank);
            output.append(" of ");
            output.append(this.suit);
        }
        else{
            output.append("FACED DOWN");
        }
        output.append("]");
        return output.toString();
    }
}
