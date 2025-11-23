import Battle.Combat;
import Decks.Deck;
import Person.*;
import PokerHands.Calculator;
import PokerHands.HandValue;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        Person a = new Person("DCG", 1.5, 1.8, 20000);
        Person b = new Joe("Joe", 16000);
        Person community = new Person("Community", 1, 1, 1);
        int turn = 0;
        while (a.getCurrentChips() > 0 && b.getCurrentChips() > 0){
            // starting a new round
            turn++;
            System.out.printf("Turn %d:%n", turn);
            Deck dck = new Deck();
            dck.shuffle();
            a.drawXFrom(dck, a.getInit_card_draw());
            b.drawXFrom(dck, b.getInit_card_draw());
            System.out.printf("%s's hand: %s%n",a.getName() ,a.getHand());
            System.out.printf("%s's hand: %s%n",b.getName() ,b.getHand());
            community.drawXFrom(dck, 5);
            System.out.printf("Community cards: %s%n",community.getHand());
            // combine player cards with community cards
            a.getHand().addAll(community.getHand());
            b.getHand().addAll(community.getHand());
            System.out.println();
            // find best hand
            HandValue hv = Calculator.evaluateAuto(a.getHand());
            System.out.println(a.getName() +  "'s hand evaluated as: " + hv);
            HandValue hv2 = Calculator.evaluateAuto(b.getHand());
            System.out.println(b.getName() +  "'s hand evaluated as: " + hv2);
            System.out.println();
            // initiate combat with stats
            Combat.resolveRound(a, hv, b, hv2);
            // discards all cards
            for (int i = 0; i< a.getInit_card_draw()+5; i++) a.discardTop();
            for (int i = 0; i< b.getInit_card_draw()+5; i++) b.discardTop();
            for (int i = 0; i< 5; i++) community.discardTop();
            // comment line below to see full result, uncomment it to advance one turn at a time with ENTER (or quick skip by holding ENTER)
            // inp.nextLine();
        }
        // display results of the match and some stats
        if (a.getCurrentChips() == 0) System.out.printf("%s has ran out of chips! %s wins with %d chips remaining%nDamage per turn: %.1f%n", a.getName(), b.getName(), b.getCurrentChips(), (double)a.getBaseChips()/turn);
        else System.out.printf("%s has ran out of chips! %s wins with %d chips remaining%nDamage per turn: %.1f%n", b.getName(), a.getName(), a.getCurrentChips(), (double)b.getBaseChips()/turn);
    }
}