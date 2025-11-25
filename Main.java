import Battle.Combat;
import Decks.Deck;
import Person.*;
import PokerHands.Calculator;
import PokerHands.HandValue;
import java.util.Scanner;

public class Main {
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
    }

    public static void dealCardsToPlayers(Person a, Person b, Person community){
        Deck dck = new Deck();
        dck.shuffle();
        a.drawXFrom(dck, a.getInit_card_draw());
        b.drawXFrom(dck, b.getInit_card_draw());
        community.drawXFrom(dck, 5);
    }

    public static void displayCommunity(Person community){
        System.out.printf("Community cards: %s\n\n",community.getHand());
    }

    public static void displayPlayerHand(Person a, HandValue hv){
        System.out.println(a.getName() +  "'s hand evaluated as: " + hv);
        System.out.println();
    }

    public static void playerAction(Person a, Scanner inp){
        System.out.println("What do you do?\n" +
                "1: Call: Play out the round like normal.\n" +
                "2: Raise: Wager 20 chips to boost your ATK by x1.5 if you win.\n" +
                "3: Fold: Spend 5 chips to triple your DEF but you are forced to defend.");
        while (true){
            int action = inp.nextInt();
            if (action == 1) a.setState("Call");
            else if (action == 2) a.setState("Raise");
            else if (action == 3) a.setState("Fold");
            else{
                System.out.println("Invalid action! Try again.\n" +
                        "1: Call: Play out the round like normal.\n" +
                        "2: Raise: Wager 20 chips to boost your ATK by x1.5 if you win\n" +
                        "3: Fold: Spend 5 chips to triple your DEF but you are forced to defend");
                continue;
            }
            break;
        }
        System.out.printf("You have chosen to %s!\n\n", a.getState());
    }

    public static HandValue calculatePlayer(Person a, Person community){
        System.out.printf("%s's hand: %s%n", a.getName() , a.getHand());
        a.getHand().addAll(community.getHand());
        HandValue hv = Calculator.evaluateAuto(a.getHand());
        System.out.println(a.getName() +  "'s hand evaluated as: " + hv);
        System.out.println();
        return hv;
    }

    public static void displayResults(Person a, Person b, int turn){
        if (a.getCurrentChips() == 0) System.out.printf("%s has ran out of chips! %s wins with %d chips remaining!%nDamage per turn: %.1f%n", a.getName(), b.getName(), b.getCurrentChips(), (double)a.getBaseChips()/turn);
        else System.out.printf("%s has ran out of chips! %s wins with %d chips remaining!%nDamage per turn: %.1f%n", b.getName(), a.getName(), a.getCurrentChips(), (double)b.getBaseChips()/turn);
    }

    public static void main(String[] args){
        Scanner inp = new Scanner(System.in);
        Person a = new Player("DCG", 1.2, 1.3, 200);
        Person b = new Joe("Joe", 160);
        Person community = new Person("Community", 1, 1, 1);
        int turn = 0;
        while (a.getCurrentChips() > 0 && b.getCurrentChips() > 0){
            // starting a new round
            turn++;
            startOfTurn(turn, a, b);
            dealCardsToPlayers(a, b, community);
            displayCommunity(community);

            // find best hand
            HandValue hv = calculatePlayer(a, community);
            playerAction(a, inp);
            HandValue hv2 = calculatePlayer(b, community);

            try {
                Thread.sleep(1000);  // 1.5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // initiate combat with stats
            Combat.resolveRound(a, hv, b, hv2);

            // discards all cards
            for (int i = 0; i< a.getInit_card_draw()+5; i++) a.discardTop();
            for (int i = 0; i< b.getInit_card_draw()+5; i++) b.discardTop();
            for (int i = 0; i< 5; i++) community.discardTop();
        }

        // display results of the match and some stats
        displayResults(a, b, turn);
    }
}