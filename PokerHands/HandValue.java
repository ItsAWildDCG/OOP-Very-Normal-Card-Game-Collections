package PokerHands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HandValue implements Comparable<HandValue> {
    private final String handType;
    private final List<Integer> highCards;

    public HandValue(String handType, List<Integer> highCards) {
        this.handType = handType;
        this.highCards = highCards;
    }

    public static final String[] Hand_Order = {
            "None", "High Cards", "One Pair", "Two Pair", "Three of a Kind", "Straight", "Flush",
            "Full House", "Four of a Kind", "Straight Flush", "Five of a Kind"
    };

    public String gethandType() { return handType; }
    public List<Integer> getHighCards() { return highCards; }

    @Override
    public int compareTo(HandValue o) {
        List<String> values = Arrays.stream(Hand_Order).toList();
        int cmp = Integer.compare(values.indexOf(handType), values.indexOf(o.gethandType()));
        if (cmp != 0) return cmp;
        for (int i = 0; i < Math.min(highCards.size(), o.highCards.size()); i++) {
            if (i == 0 && handType.equals("Straight")){
                if (highCards.get(i) == 14 || o.highCards.get(i)== 14) return  o.highCards.get(i) - highCards.get(i);
            }
            if (highCards.get(i) != 1 && o.highCards.get(i)== 1) return -1;
            int diff = highCards.get(i) - o.highCards.get(i);
            if (diff != 0) return diff;
        }
        return 0;
    }

    @Override
    public String toString() {
        return handType + " " + highCards;
    }

    public String getHandType() {
        return handType;
    }
}