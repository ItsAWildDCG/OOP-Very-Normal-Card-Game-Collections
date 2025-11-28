import Battle.Combat;
import Cards.Card;
import Decks.Deck;
import Person.*;
import PokerHands.Calculator;
import PokerHands.HandValue;
import Items.*;

import java.io.*;
import java.util.*;

public class Main {
    static Random rdm = new Random();
    static List<Item> items = new ArrayList<>();
    static Person community = new Person("Community", 1, 1, 1);
    static List<Card> discardpile = new ArrayList<>();
    static Deck dck = new Deck(false);
    static Scanner inp = new Scanner(System.in);
    static int discards = 2;
    static int round;
    static int turn;
    static Person[] gauntlet;
    static Person current;
    static Player you;
    static String state = "";
    
    public static void startOfTurn(){
        System.out.printf("Turn %d: ", turn);
        if (turn > 10) System.out.printf("Big blind. -%d chips every turn.\n", turn/2);
        else if (turn > 5) System.out.print("Small blind. -3 chips every turn.\n");
        else System.out.println();
        System.out.print("Active buffs: ");
        if (you.getVampbuff()>0) System.out.printf("Lifesteal (%d attack(s)) ", you.getVampbuff());
        if (you.getBlindbuff()>0) System.out.printf("Blind delay (%d turn(s)) ", you.getBlindbuff());
        if (you.getVampbuff()==0 && you.getBlindbuff()==0) System.out.print("None");
        System.out.println();
        if (!state.equals("Continue")){
            if (turn > 10){
                if (you.getBlindbuff()>0) you.changeBlindbuff(-1);
                else you.changeChips(-turn/2);
                current.changeChips(-turn/2);
            }
            else if (turn > 5) {
                if (you.getBlindbuff()>0) you.changeBlindbuff(-1);
                else you.changeChips(-3);
                current.changeChips(-3);
            }
        }
        if (you.getCurrentChips() <= 0) you.changeChips(1);
        if (current.getCurrentChips() <= 0) current.changeChips(1);
        System.out.println(you);
        System.out.println(current);
        System.out.println();
    }

    public static void dealCardsToPlayers(){
        dck.shuffle();
        you.drawXFrom(dck, you.getInit_card_draw());
        current.drawXFrom(dck, current.getInit_card_draw());
        community.drawXFrom(dck, 5);
    }

    public static void displayCommunity(){
        System.out.printf("Community cards: %s\n",community.getHand());
        System.out.printf("Cards left in deck: %d\n\n", dck.check().size());
    }

    public static void playerAction(){
        while (true){
            displayCommunity();
            calculatePlayer(you);
            System.out.printf("What do you do?\n" +
                    "1: Call: Play out the round like normal.\n" +
                    "2: Raise: Wager 20 chips to boost your ATK by x1.5 if you win.\n" +
                    "3: Fold: Spend 5 chips to triple your DEF but you are forced to defend.\n" +
                    "4: Discard (" + discards + " left): Remove up to two cards in your hand and draw that many.\n" +
                    "5: Item (%d): Use an item to change the tides of battle.\n" +
                    "\n" +
                    "0: Save and Quit\n", items.size());
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
                state = "Quit";
                saveAndQuit();
                break;
            }
            else if (action == 1) state = "Call";
            else if (action == 2) state = "Raise";
            else if (action == 3) state = "Fold";
            else if (action == 4) {
                discard();
                continue;
            }
            else{
                useItem();
                continue;
            }
            break;
        }
        if (state.equals("Quit")) return;
        System.out.printf("You have chosen to %s!\n\n", state);
    }

    public static void discard(){
        inp.nextLine();
        if (discards == 0){
            System.out.println("No more discards!\n");
            return;
        }
        System.out.println("Select card positions to discard (1 to n from left to right e.g. \"1 2\"): ");
        Set<String> str = new TreeSet<>(Arrays.asList(inp.nextLine().trim().split("\\s+")));
        try{
            Set<Integer> remove = new TreeSet<>();
            for (String s: str) remove.add(Integer.parseInt(s));

            if (remove.isEmpty() || remove.size()>2) throw new Exception();
            if(new ArrayList<>(remove).getFirst()<1||new ArrayList<>(remove).getLast()>you.size()) throw new Exception();

            int discarded = 0;
            for (int i: new ArrayList<>(remove).reversed()){
                discardpile.add(you.getHand().remove(i-1));
                discarded++;
            }
            you.drawXFrom(dck, discarded);

        } catch (Exception e) {
            System.out.println("Invalid discard attempt.");
            return;
        }
        discards--;
    }
    
    public static void useItem(){
        int pos = 1;
        if (items.isEmpty()){
            System.out.println("You currently have no items.");
            return;
        }
        System.out.println("Choose an item to use:");
        for (Item i: items){
            System.out.printf("%d: %s%n", pos, i);
            pos++;
        }
        System.out.print("\n0: Return to actions.\n");
        int use = 0;
        try{
            use = inp.nextInt();
            if (use == 0) return;
            switch(items.get(use-1).getName()){
                case "Broken Magnifier":
                    System.out.println("Select the position of the card you wish to see (your opponent has " + current.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        System.out.println("The opponent has " + selectCard(current, view) + " in their hand.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "+1 Uno Card":
                    you.drawFrom(dck);
                    System.out.println("Drew an extra card.");
                    break;
                case "One For @Everyone":
                    you.drawFrom(dck);
                    current.drawFrom(dck);
                    community.drawFrom(dck);
                    System.out.println("Everyone drew an extra card.");
                    break;
                case "No more middle":
                    System.out.println("Select the position of the card you wish to discard (there are " + community.size() + " community cards):");
                    try{
                        int view = inp.nextInt();
                        discardpile.add(community.getHand().remove(view-1));
                        System.out.println("Card has been removed.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Adoption Paper":
                    community.drawFrom(dck);
                    System.out.println("Additional card drawn to the table.");
                    break;
                case "Cursed Scroll":
                    current.changeChips(-current.getBaseChips()/10);
                    if (current.getCurrentChips() == 0) current.changeChips(1);
                    System.out.println("Enemy took 10% of max chips.");
                    break;
                case "Mr Krabz's Greed":
                    you.getHand().add(new Card(2, "Diamonds"));
                    you.getHand().add(new Card(2, "Diamonds"));
                    System.out.println("Created 2 Two of Diamonds.");
                    break;
                case "Doppleganger":
                    System.out.println("Select the position of the card you wish to duplicate (you have " + you.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        dck.add_card(selectCard(you, view));
                        you.drawFrom(dck);
                        System.out.println("Card duplicated.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Kings of King":
                    for (Card i: community.getHand()){
                        if (i.getRank() == 11 || i.getRank() == 12){
                            i.setRank(13);
                            i.updateName();
                        }
                    }
                    System.out.println("All community face cards are now Kings.");
                    break;
                case "Alu-card's Battlepass":
                    you.changeVampbuff(1);
                    System.out.println("Your next attack now heals you.");
                    break;
                case "I lose this, you lose that":
                    System.out.println("Select the position of the card you wish to remove (you have " + you.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        discardpile.add(you.discard(selectCard(you, view)));
                        discardpile.add(current.discard(selectCard(current, rdm)));
                        System.out.println("Cards from both player discarded.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "CSync":
                    System.out.println("Select the position of the card you wish to copy suit from (you have " + you.size() + " cards in hand and there are " + community.size() + " community cards):");
                    try{
                        int view = inp.nextInt();
                        String suittocopy;
                        if (view > you.size()) suittocopy = selectCard(community, view-you.size()).getSuit();
                        else suittocopy = selectCard(you, view).getSuit();
                        for (Card c: you.getHand()) c.setSuit(suittocopy);
                        System.out.println("Hand's suits changed.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Demotion":
                    System.out.println("Select the position of the card you wish to lower rank (you have " + you.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        if (selectCard(you, view).getRank() == 2) selectCard(you, view).changeRank(12);
                        else selectCard(you, view).changeRank(-1);
                        System.out.println("Rank of card lowered.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Strength":
                    System.out.println("Select the position of the card you wish to raise rank (you have " + you.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        if (selectCard(you, view).getRank() == 14) selectCard(you, view).changeRank(-12);
                        else selectCard(you, view).changeRank(1);
                        System.out.println("Rank of card raised.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Pile of cash":
                    you.changeChips(you.getBaseChips()/5);
                    System.out.println("Regained 20% of max chips.");
                    break;
                case "Madness Potion":
                    you.changeBlindbuff(3);
                    System.out.println("You will now nullify the next 3 turns worth of blind damage.");
                    break;
                case "Slate of *null*":
                    dck.peek().setWild(true);
                    you.drawFrom(dck);
                    System.out.println("Made and drew a wild card.");
                    break;
                case "Fairy's tooth":
                    you.changeChips(-you.getBaseChips()/10);
                    you.drawXFrom(dck, 3);
                    System.out.println("Drew 3 extra cards in exchange for " + (you.getBaseChips()/10) + " chips.");
                    break;
                case "Rainbow":
                    boolean ok = true;
                    int drawcount = 0;
                    Set<String> unique = new TreeSet<>();
                    for (Card i: you.getHand()){
                        if (!unique.contains(i.getSuit())&&!i.isWild()) unique.add(i.getSuit());
                        else{
                            ok = false;
                            break;
                        }
                    }
                    while(ok){
                        you.drawFrom(dck);
                        drawcount++;
                        if (!unique.contains(you.getHand().getLast().getSuit())&&!you.getHand().getLast().isWild()) unique.add(you.getHand().getLast().getSuit());
                        else{
                            ok = false;
                        }
                    }
                    System.out.println("Drew a total of " + drawcount + " cards.");
                    break;
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Invalid item usage.");
            return;
        }
        items.remove(use-1);
    }

    public static Card selectCard(Person a, int pos){
        return a.getHand().get(pos-1);
    }

    public static Card selectCard(Person a, Random rdm){
        return a.getHand().get(rdm.nextInt(a.size()));
    }

    public static boolean continueGame() {
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
                    case "Breno":
                        gauntlet[i] = new Breno(nameboss, savefile.nextInt(), turn-1);
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
            you = new Player(savefile.nextLine(), savefile.nextDouble(), savefile.nextDouble(), savefile.nextInt(), savefile.nextInt());
            you.changeVampbuff(savefile.nextInt());
            you.changeBlindbuff(savefile.nextInt());
            savefile.nextLine();
            int itemsize = savefile.nextInt();
            savefile.nextLine();
            while(itemsize-->0) itemGen(savefile.nextLine());
            int playersize = savefile.nextInt();
            savefile.nextLine();
            while(playersize-->0) you.getHand().add(new Card(savefile.nextLine()));
            while(savefile.hasNextLine()) dck.add_card(new Card(savefile.nextLine()));
            System.out.println("Load successful!");
            state = "Continue";
            startOfTurn();
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
            savefile.write(current.size()+"\n");
            for (Card c: current.getHand()) savefile.write(c + "\n");
            savefile.write(community.size()+"\n");
            for (Card c: community.getHand()) savefile.write(c + "\n");
            savefile.write(you.getName()+"\n");
            savefile.write(String.format("%.1f %.1f %d %d %d %d\n", you.getAtkMult(), you.getDefMult(), you.getCurrentChips() ,you.getBaseChips(), you.getVampbuff(), you.getBlindbuff()));
            savefile.write(items.size()+"\n");
            for (Item i: items) savefile.write(i.getName() + "\n");
            savefile.write(you.size()+"\n");
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

    public static void displayResults(){
        if (round > gauntlet.length) System.out.printf("Congratulations! %s has defeated every boss and become the ultimate gambler with %d chips to spare!!!!\n", you.getName(), you.getCurrentChips());
        else System.out.printf("%s has ran out of chips in round %d... %s wins with %d chips remaining. Better luck next time!\n", you.getName(), round, current.getName(), current.getCurrentChips());
        try (FileWriter savefile = new FileWriter("Saves/game.sav", false)){
            savefile.write("NO DATA");
        }
        catch (Exception _){}
    }

    public static void finishTurn(HandValue hv, HandValue hv2){
        Combat.resolveRound(you, hv, current, hv2, state);
        // discards all cards
        while (!you.hasNoCards()) dck.add_card(you.discardTop());
        while (!current.hasNoCards()) dck.add_card(current.discardTop());
        while (!community.hasNoCards()) dck.add_card(community.discardTop());
        while (!discardpile.isEmpty()) dck.add_card(discardpile.removeFirst());
        dck.shuffle();
        if (hv.compareTo(hv2) < 0 || state.equals("Fold")) discards = 2;
        if (current.getCurrentChips() == 0){
            finishRound();
            return;
        }
        current.uniqueAction();
    }

    public static void finishRound(){
        System.out.printf("You won! Gained %d chips and %d items! Proceeding to next battle.\n", you.getBaseChips()/2, 2+round);
        you.changeChips(you.getBaseChips()/2);
        for (int i = 0; i<2+round; i++) itemGen("Random");
        turn = 0;
        round++;
        if (round > gauntlet.length) return;
        current = gauntlet[round-1];
        dck = new Deck();
        System.out.printf("****************ROUND %d****************\n\n", round);
    }

    public static void itemGen(String i){
        String nextItem;
        if (i.equals("Random"))  nextItem = Item.Item_Names[rdm.nextInt(19)];
        else nextItem = i;
        switch(nextItem){
            case "Broken Magnifier":
                items.add(new BrokenMag());
                break;
            case "+1 Uno Card":
                items.add(new CardDraw());
                break;
            case "One For @Everyone":
                items.add(new Charity());
                break;
            case "No more middle":
                items.add(new CommunityCardDeduct());
                break;
            case "Adoption Paper":
                items.add(new CommunityCardDraw());
                break;
            case "Cursed Scroll":
                items.add(new CursedScroll());
                break;
            case "Mr Krabz's Greed":
                items.add(new Dia2());
                break;
            case "Doppleganger":
                items.add(new Duplic());
                break;
            case "Kings of King":
                items.add(new Kinged());
                break;
            case "Alu-card's Battlepass":
                items.add(new Lifesteal());
                break;
            case "I lose this, you lose that":
                items.add(new Loss());
                break;
            case "CSync":
                items.add(new Melatonin());
                break;
            case "Demotion":
                items.add(new RankDec());
                break;
            case "Strength":
                items.add(new RankInc());
                break;
            case "Pile of cash":
                items.add(new RegainChips());
                break;
            case "Madness Potion":
                items.add(new Singed());
                break;
            case "Slate of *null*":
                items.add(new Slate());
                break;
            case "Fairy's tooth":
                items.add(new Toothfairy());
                break;
            case "Rainbow":
                items.add(new UnoPickMoreCards());
                break;
        }
    }

    public static void main(String[] args) {
        if (!continueGame()){
            items = new ArrayList<>();
            gauntlet = new Person[4];
            community = new Person("community", 1);
            you = new Player("DCG", 1.2, 1.3, 100);
            gauntlet[0] = new Person("Fred", 1.0, 1.0, 150);
            gauntlet[1] = new Person("Biden", 1.1, 1.1, 200);
            gauntlet[2] = new Breno("Breno", 160);
            gauntlet[3] = new Joe("Joe", 250);
            round = 1;
            discards = 2;
            current = gauntlet[0];
            turn = 0;
            dck = new Deck();
            System.out.printf("****************ROUND %d****************\n\n", round);
        }
        while (you.getCurrentChips() > 0){
            if (!state.equals("Continue")){
                turn++;
                startOfTurn();
                dealCardsToPlayers();
            }

            playerAction();

            if (state.equals("Quit")) break;
            HandValue hv = calculatePlayer(you);
            HandValue hv2 = calculatePlayer(current);

            try {
                Thread.sleep(1000);  // 1.5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // initiate combat with stats
            finishTurn(hv, hv2);
            if (round > gauntlet.length) break;
        }
        if (state.equals("Quit")) return;
        // display results of the adventure
        displayResults();
    }
}