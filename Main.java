import Battle.Combat;
import Cards.Card;
import Decks.Deck;
import Person.*;
import PokerHands.Calculator;
import PokerHands.HandValue;


import java.io.*;
import java.util.*;

public class Main {
    static Person community = new Person("Community", 1, 1, 1);
    static List<Card> discardpile = new ArrayList<>();
    static Deck dck = new Deck(false);
    static Scanner inp = new Scanner(System.in);
    static int discards = 2;
    static int round;
    static int turn;
    static Person[] gauntlet;
    static Person current;
    static Person you;
    public static void startOfTurn(Person a, Person b){
        System.out.printf("Turn %d: ", turn);
        if (turn > 10) System.out.printf("Big blind. -%d chips every turn.\n", turn/2);
        else if (turn > 5) System.out.print("Small blind. -3 chips every turn.\n");
        else System.out.println();
        if (!you.getState().equals("Continue")){
            if (turn > 10){
                a.changeChips(-turn/2);
                b.changeChips(-turn/2);
            }
            else if (turn > 5) {
                a.changeChips(-3);
                b.changeChips(-3);
            }
        }
        if (a.getCurrentChips() <= 0) a.changeChips(1);
        if (b.getCurrentChips() <= 0) b.changeChips(1);
        System.out.println(a);
        System.out.println(b);
        System.out.println();
    }

    public static void dealCardsToPlayers(Person a, Person b){
        dck.shuffle();
        a.drawXFrom(dck, a.getInit_card_draw());
        b.drawXFrom(dck, b.getInit_card_draw());
        community.drawXFrom(dck, 5);
    }

    public static void displayCommunity(){
        System.out.printf("Community cards: %s\n\n",community.getHand());
    }

    public static void playerAction(){
        while (true){
            System.out.println("What do you do?\n" +
                    "1: Call: Play out the round like normal.\n" +
                    "2: Raise: Wager 20 chips to boost your ATK by x1.5 if you win.\n" +
                    "3: Fold: Spend 5 chips to triple your DEF but you are forced to defend.\n" +
                    "4: Discard (" + discards + " left): Remove up to two cards in your hand and draw that many.\n" +
                    "5: Item: Use an item to change the tides of battle.\n" +
                    "\n" +
                    "0: Save and Quit");
            int action;
            try{
                action = inp.nextInt();
                if (action < 0 || action > 5){
                    throw new Exception();
                }
            }
            catch (Exception e){
                System.out.println("Invalid action! Try again.");
                continue;
            }
            if (action == 0){
                you.setState("Quit");
                saveAndQuit();
                break;
            }
            else if (action == 1) you.setState("Call");
            else if (action == 2) you.setState("Raise");
            else if (action == 3) you.setState("Fold");
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

                    if(new ArrayList<>(remove).getFirst()<1||new ArrayList<>(remove).getLast()>you.getInit_card_draw()){
                        throw new Exception();
                    }

                    int discarded = 0;
                    for (int i: new ArrayList<>(remove).reversed()){
                        discardpile.add(you.getHand().remove(i-1));
                        discarded++;
                    }
                    you.drawXFrom(dck, discarded);

                    displayCommunity();
                    calculatePlayer(you);

                } catch (Exception e) {
                    System.out.println("Invalid discard attempt.");
                    continue;
                }
                discards--;
                continue;
            }
            else{
                System.out.println("No item implementations yet! Stay tuned.");
                continue;
            }
            break;
        }
        if (you.getState().equals("Quit")) return;
        System.out.printf("You have chosen to %s!\n\n", you.getState());
    }
    public static boolean continueGame() throws FileNotFoundException {
        try (Scanner savefile = new Scanner (new File("Saves/game.sav"))){
            if (!savefile.hasNextInt()) throw new Exception();
            System.out.println("A saved game has been found. Would you like to continue? (y/n)");
            while(true){
                String play = inp.nextLine();
                if(play.equals("n")) return false;
                if(play.equals("y")) break;
            }
            discards = savefile.nextInt();
            round = savefile.nextInt();
            turn = savefile.nextInt();
            gauntlet = new Person[savefile.nextInt()];
            savefile.nextLine();
            String nameboss;
            for (int i = 0; i< gauntlet.length; i++) {
                nameboss = savefile.nextLine();
                switch(nameboss){
                    case "Joe":
                        gauntlet[i] = new Joe(nameboss, savefile.nextInt());
                        break;
                    default:
                        gauntlet[i] = new Person(nameboss, savefile.nextInt());
                }
                savefile.nextLine();
            }
            current = gauntlet[round-1];
            current.changeChips(savefile.nextInt()-current.getBaseChips());
            int currentsize = savefile.nextInt();
            savefile.nextLine();
            while(currentsize-->0) current.getHand().add(new Card(savefile.nextLine()));
            int communitysize = savefile.nextInt();
            savefile.nextLine();
            while(communitysize-->0) community.getHand().add(new Card(savefile.nextLine()));
            you = new Person(savefile.nextLine(), savefile.nextDouble(), savefile.nextDouble(), savefile.nextInt(), savefile.nextInt());
            savefile.nextLine();
            int playersize = savefile.nextInt();
            savefile.nextLine();
            while(playersize-->0) you.getHand().add(new Card(savefile.nextLine()));
            while(savefile.hasNextLine()) dck.add_card(new Card(savefile.nextLine()));
            System.out.println("Load successful!");
            you.setState("Continue");
            startOfTurn(you, current);
            return true;
        }
        catch (Exception e){
            System.out.println("Save data unavailable or corrupted. Starting a new game.");
            return false;
        }
    }
    public static void saveAndQuit() {
        try (FileWriter savefile = new FileWriter("Saves/game.sav", false)){
            savefile.write(String.format("%d %d %d\n", discards, round, turn));
            savefile.write(gauntlet.length + "\n");
            for (Person p : gauntlet) {
                savefile.write(String.format(p.getName() + "\n"));
                savefile.write(String.format("%d\n",  p.getBaseChips()));
            }
            savefile.write(current.getCurrentChips() + "\n");
            savefile.write(current.getHand().size()+"\n");
            for (Card c: current.getHand()) savefile.write(c + "\n");
            savefile.write(community.getHand().size()+"\n");
            for (Card c: community.getHand()) savefile.write(c + "\n");
            savefile.write(you.getName()+"\n");
            savefile.write(String.format("%.1f %.1f %d %d %d\n", you.getAtkMult(), you.getDefMult(), you.getCurrentChips() ,you.getBaseChips(), you.getInit_card_draw()));
            savefile.write(you.getHand().size()+"\n");
            for (Card c: you.getHand()) savefile.write(c + "\n");
            for (Card c : dck.check()) savefile.write(c + "\n");
            System.out.println("Saved your progress! Now quitting.");
        }
        catch (Exception e){
            System.out.println("Saving failed. Quitting without saving.");
        }
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
        if (round > 3) System.out.printf("Congratulations! %s has defeated every boss and become the ultimate gambler with %d chips to spare!!!!\n", a.getName(), a.getCurrentChips());
        else System.out.printf("%s has ran out of chips in round %d... %s wins with %d chips remaining. Better luck next time!\n", a.getName(), round, b.getName(), b.getCurrentChips());
        try (FileWriter savefile = new FileWriter("Saves/game.sav", false)){
            savefile.write("NO DATA");
        }
        catch (Exception _){}
    }

    public static void finishRound(HandValue hv, HandValue hv2){
        Combat.resolveRound(you, hv, current, hv2);
        // discards all cards
        while (!you.hasNoCards()) dck.add_card(you.discardTop());
        while (!current.hasNoCards()) dck.add_card(current.discardTop());
        while (!community.hasNoCards()) dck.add_card(community.discardTop());
        while (!discardpile.isEmpty()) dck.add_card(discardpile.removeFirst());
        dck.shuffle();
        if (hv.compareTo(hv2) < 0 || you.getState().equals("Fold")) discards = 2;
        if (current.getCurrentChips() == 0){
            System.out.printf("You won! Gained %d chips! Proceeding to next battle.\n", you.getBaseChips()/2);
            you.changeChips(you.getBaseChips()/2);
            turn = 0;
            round++;
            if (round > 3) return;
            current = gauntlet[round-1];
            dck = new Deck();
            System.out.printf("****************ROUND %d****************\n\n", round);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (!continueGame()){
            gauntlet = new Person[4];
            community = new Person("community", 1);
            you = new Player("DCG", 1.2, 1.3, 100);
            gauntlet[0] = new Person("Fred", 1.0, 1.0, 150);
            gauntlet[1] = new Person("Biden", 1.1, 1.1, 200);
            gauntlet[2] = new Joe("Joe", 160);
            gauntlet[3] = new Joe("Joe", 250);
            round = 1;
            System.out.printf("****************ROUND %d****************\n\n", round);
            current = gauntlet[0];
            turn = 0;
            dck = new Deck();
        }
        while (you.getCurrentChips() > 0){
            if (!you.getState().equals("Continue")){
                turn++;
                startOfTurn(you, current);
                dealCardsToPlayers(you, current);
            }

            displayCommunity();
            calculatePlayer(you);
            playerAction();

            if (you.getState().equals("Quit")) break;
            HandValue hv = calculatePlayer(you);
            HandValue hv2 = calculatePlayer(current);

            try {
                Thread.sleep(1000);  // 1.5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // initiate combat with stats
            finishRound(hv, hv2);
            if (round > 3) break;
        }
        if (you.getState().equals("Quit")) return;
        // display results of the adventure
        displayResults(you, current, round);
    }
}