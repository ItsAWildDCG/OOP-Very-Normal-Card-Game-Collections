package PokerHands;

import Cards.Card;

import java.util.*;
import java.util.stream.Collectors;

public class Calculator {

    public static HandValue evaluateAuto(List<Card> cards) {
        if (cards.size() <= 5)
            return evaluate(cards);

        HandValue best = null;
        List<int[]> combos = generateCombinations(cards.size(), 5);
        for (int[] c : combos) {
            List<Card> subset = new ArrayList<>();
            for (int i : c) subset.add(cards.get(i));
            HandValue hv = evaluate(subset);
            if (best == null || hv.compareTo(best) > 0) best = hv;
        }
        return best;
    }

    private static HandValue evaluate(List<Card> cards) {
        // Count ranks
        Map<Integer, Long> rankCounts = cards.stream().collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // Check for flush (wilds add to all suits)
        boolean flush = isFlush(cards);

        // Check for straight
        boolean straight = isStraight(cards);

        // Check for multiples
        List<Long> counts = new ArrayList<>(rankCounts.values());
        counts.sort(Collections.reverseOrder());
        String rank = getString(counts, straight, flush);

        // Sort high cards for comparison
        List<Integer> sorted = cards.stream()
                .map(Card::getRank)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());


        return new HandValue(rank, sorted);
    }

    private static String getString(List<Long> counts, boolean straight, boolean flush) {
        long max = counts.get(0);

        String rank;
        if (max == 5) rank = "Five of a kind";
        else if (straight && flush) rank = "Straight Flush";
        else if (max == 4) rank =  "Four of a Kind";
        else if (counts.size() >= 2 && counts.get(0) == 3 && counts.get(1) >= 2) rank = "Full House";
        else if (flush) rank = "Flush";
        else if (straight) rank = "Straight";
        else if (max == 3) rank = "Three of a Kind";
        else if (Collections.frequency(counts, 2L) == 2)  rank = "Two Pair";
        else if (Collections.frequency(counts, 2L) == 1) rank = "One Pair";
        else if (max == 1 ) rank = "High Cards";
        else rank = "None";
        return rank;
    }

    private static boolean isFlush(List<Card> cards) {
        Map<String, Long> suitCounts = new HashMap<>();
        int wildCount = 0;

        for (Card c : cards) {
            if (c.isWild()) {
                wildCount++;
                continue;
            }
            suitCounts.put(c.getSuit(), suitCounts.getOrDefault(c.getSuit(), 0L) + 1);
        }

        for (String s : Card.Suit_names) {
            long total = suitCounts.getOrDefault(s, 0L) + wildCount;
            if (total >= 5) return true;
        }
        return false;
    }

    private static boolean isStraight(List<Card> cards) {
        Set<Integer> unique = cards.stream().map(Card::getRank).collect(Collectors.toSet());

        List<Integer> sorted = new ArrayList<>(unique);
        Collections.sort(sorted);

        int consec = 1;
        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i) == sorted.get(i - 1) + 1) {
                consec++;
                if (consec >= 5) return true;
            } else {
                consec = 1;
            }
        }
        return (unique.contains(14) && unique.containsAll(Set.of(2,3,4,5)));
    }

    private static List<int[]> generateCombinations(int n, int k) {
        List<int[]> result = new ArrayList<>();
        int[] combo = new int[k];
        for (int i = 0; i < k; i++) combo[i] = i;
        while (combo[k - 1] < n) {
            result.add(combo.clone());
            int t = k - 1;
            while (t != 0 && combo[t] == n - k + t) t--;
            combo[t]++;
            for (int i = t + 1; i < k; i++) combo[i] = combo[i - 1] + 1;
        }
        return result;
    }
}
