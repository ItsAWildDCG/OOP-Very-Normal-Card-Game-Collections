import Battle.Combat;
import Cards.Card;
import Decks.Deck;
import Person.*;
import PokerHands.Calculator;
import PokerHands.HandValue;

import java.util.*;

public class Main {
    static Person community = new Person("Community", 1, 1, 1);
    static Deck dck;
    static Scanner inp = new Scanner(System.in);
    static int discards = 0;
    public static void startOfTurn(int turncount, Person a, Person b){
        System.out.printf("Turn %d: ", turncount);
        if (turncount > 10) {
            System.out.printf("Big blind. -%d chips every turn.\n", turncount);
            a.changeChips(-turncount);
            b.changeChips(-turncount);
        }
        else if (turncount > 5){
            System.out.print("Small blind. -5 chips every turn.\n");
            a.changeChips(-5);
            b.changeChips(-5);
        }
        else System.out.println();
        if (a.getCurrentChips() <= 0) a.changeChips(-a.getCurrentChips()+1);
        if (b.getCurrentChips() <= 0) b.changeChips(-b.getCurrentChips()+1);
        System.out.println(a);
        System.out.println(b);
        System.out.println();
        discards = Math.min(discards+1, 2);
    }

    public static void dealCardsToPlayers(Person a, Person b){
        dck = new Deck();
        dck.shuffle();
        a.drawXFrom(dck, a.getInit_card_draw());
        b.drawXFrom(dck, b.getInit_card_draw());
        community.drawXFrom(dck, 5);
    }

    public static void displayCommunity(){
        System.out.printf("Community cards: %s\n\n",community.getHand());
    }


    public static void playerAction(Person a){
        while (true){
            System.out.println("What do you do?\n" +
                    "1: Call: Play out the round like normal.\n" +
                    "2: Raise: Wager 20 chips to boost your ATK by x1.5 if you win.\n" +
                    "3: Fold: Spend 5 chips to triple your DEF but you are forced to defend.\n" +
                    "4: Discard (" + discards + " left): Remove up to two cards in your hand and draw that many.");
            int action = inp.nextInt();
            if (action == 1) a.setState("Call");
            else if (action == 2) a.setState("Raise");
            else if (action == 3) a.setState("Fold");
            else if (action == 4){
                inp.nextLine();
                if (discards == 0){
                    System.out.println("No more discards!\n");
                    continue;
                }
                System.out.println("Select card positions to discard (1 to n from left to right e.g. \"1 2\"): ");
                Set<String> str = new TreeSet<>(Arrays.asList(inp.nextLine().trim().split("\\s+")));
                try{
                    Set<Integer> remove = new TreeSet<>();
                    for (String s: str) remove.add(Integer.parseInt(s));
                    int discarded = 0;
                    for (int i: new ArrayList<>(remove).reversed()){
                        a.getHand().remove(i-1);
                        discarded++;
                    }
                    a.drawXFrom(dck, discarded);

                    displayCommunity();
                    calculatePlayer(a);

                } catch (Exception e) {
                    System.out.println("Invalid discard attempt.");
                    continue;
                }
                discards--;
                continue;
            }
            else{
                System.out.println("Invalid action! Try again.");
                continue;
            }
            break;
        }
        System.out.printf("You have chosen to %s!\n\n", a.getState());
    }

    public static HandValue calculatePlayer(Person a){
        System.out.printf("%s's hand: %s%n", a.getName() , a.getHand());
        List<Card> temp = new ArrayList<>();
        temp.addAll(a.getHand());
        temp.addAll(community.getHand());
        HandValue hv = Calculator.evaluateAuto(temp);
        System.out.println(a.getName() +  "'s hand evaluated as: " + hv);
        System.out.println();
        return hv;
    }

    public static void displayResults(Person a, Person b, int round){
        if (round == 3) System.out.printf("Congratulations! %s has defeated every boss and become the ultimate gambler with %d chips to spare!!!!\n", a.getName(), a.getCurrentChips());
        else System.out.printf("%s has ran out of chips in round %d... %s wins with %d chips remaining. Better luck next time!\n", a.getName(), round, b.getName(), b.getCurrentChips());
    }

    public static void main(String[] args){
        Person[] gauntlet = new Person[3];
        Person you = new Player("DCG", 1.2, 1.3, 250);
        gauntlet[0] = new Joe("Joe", 160);
        gauntlet[1] = new Joe("Joe", 240);
        gauntlet[2] = new Joe("Joe", 350);
        int round = 1;
        System.out.printf("****************ROUND %d****************\n\n", round);
        Person current = gauntlet[0];
        int turn = 0;
        while (you.getCurrentChips() > 0){
            // starting a new round
            turn++;
            startOfTurn(turn, you, current);
            dealCardsToPlayers(you, current);
            displayCommunity();

            // find best hand
            calculatePlayer(you);
            playerAction(you);
            HandValue hv = calculatePlayer(you);
            HandValue hv2 = calculatePlayer(current);

            try {
                Thread.sleep(1000);  // 1.5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // initiate combat with stats
            Combat.resolveRound(you, hv, current, hv2);

            // discards all cards
            for (int i = 0; i< you.getInit_card_draw(); i++) you.discardTop();
            for (int i = 0; i< current.getInit_card_draw(); i++) current.discardTop();
            for (int i = 0; i< 5; i++) community.discardTop();
            if (current.getCurrentChips() == 0){
                System.out.printf("You won! Gained %d chips! Proceeding to next battle.\n", you.getBaseChips()/2);
                you.changeChips(you.getBaseChips()/2);
                turn = 0;
                round++;
                if (round > 3) break;
                current = gauntlet[round-1];
                System.out.printf("****************ROUND %d****************\n\n", round);
            }
        }

        // display results of the adventure
        displayResults(you, current, round);
    }
}