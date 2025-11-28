package Manager;

import Battle.Combat;
import Cards.Card;
import Decks.Deck;
import Items.Item;
import Person.Person;
import PokerHands.HandValue;
import Saves.SaveData;
import Saves.Saver;

import java.io.FileWriter;
import java.util.*;

import static PokerHands.Calculator.calculatePlayer;

public class ActionManager {
    public static void startOfTurn(SaveData data){
        System.out.printf("Turn %d: ", data.turn);
        if (data.turn > 10) System.out.printf("Big blind. -%d chips every turn.\n", data.turn/2);
        else if (data.turn > 5) System.out.print("Small blind. -3 chips every turn.\n");
        else System.out.println();
        System.out.print("Active buffs: ");
        if (data.you.getVampbuff()>0) System.out.printf("Lifesteal (%d attack(s)) ", data.you.getVampbuff());
        if (data.you.getBlindbuff()>0) System.out.printf("Blind delay (%d turn(s)) ", data.you.getBlindbuff());
        if (data.you.getVampbuff()==0 && data.you.getBlindbuff()==0) System.out.print("None");
        System.out.println();
        if (!data.state.equals("Continue")){
            if (data.turn > 10){
                if (data.you.getBlindbuff()>0) data.you.changeBlindbuff(-1);
                else data.you.changeChips(-data.turn/2);
                data.current.changeChips(-data.turn/2);
            }
            else if (data.turn > 5) {
                if (data.you.getBlindbuff()>0) data.you.changeBlindbuff(-1);
                else data.you.changeChips(-3);
                data.current.changeChips(-3);
            }
        }
        if (data.you.getCurrentChips() <= 0) data.you.changeChips(1);
        if (data.current.getCurrentChips() <= 0) data.current.changeChips(1);
        System.out.println(data.you);
        System.out.println(data.current);
        System.out.println();
    }

    public static void dealCardsToPlayers(SaveData data){
        data.dck.shuffle();
        data.you.drawXFrom(data.dck, data.you.getInit_card_draw());
        data.current.drawXFrom(data.dck, data.current.getInit_card_draw());
        data.community.drawXFrom(data.dck, 5);
    }

    public static void displayCommunity(SaveData data){
        System.out.printf("Community cards: %s\n",data.community.getHand());
        System.out.printf("Cards left in deck: %d\n\n", data.dck.check().size());
    }

    public static void playerAction(SaveData data, Scanner inp, Random rdm){
        while (true){
            displayCommunity(data);
            calculatePlayer(data.you, data.community);
            System.out.printf("What do you do?\n" +
                    "1: Call: Play out the round like normal.\n" +
                    "2: Raise: Wager 20 chips to boost your ATK by x1.5 if you win.\n" +
                    "3: Fold: Spend 5 chips to triple your DEF but you are forced to defend.\n" +
                    "4: Discard (" + data.discards + " left): Remove up to two cards in your hand and draw that many.\n" +
                    "5: Item (%d): Use an item to change the tides of battle.\n" +
                    "\n" +
                    "0: Save and Quit\n", data.items.size());
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
                data.state = "Quit";
                Saver.save(data);
                break;
            }
            else if (action == 1) data.state = "Call";
            else if (action == 2) data.state = "Raise";
            else if (action == 3) data.state = "Fold";
            else if (action == 4) {
                discard(data, inp);
                continue;
            }
            else{
                useItem(data, inp, rdm);
                continue;
            }
            break;
        }
        if (data.state.equals("Quit")) return;
        System.out.printf("You have chosen to %s!\n\n", data.state);
    }

    public static void discard(SaveData data, Scanner inp){
        inp.nextLine();
        if (data.discards == 0){
            System.out.println("No more discards!\n");
            return;
        }
        System.out.println("Select card positions to discard (1 to n from left to right e.g. \"1 2\"): ");
        Set<String> str = new TreeSet<>(Arrays.asList(inp.nextLine().trim().split("\\s+")));
        try{
            Set<Integer> remove = new TreeSet<>();
            for (String s: str) remove.add(Integer.parseInt(s));

            if (remove.isEmpty() || remove.size()>2) throw new Exception();
            if(new ArrayList<>(remove).getFirst()<1||new ArrayList<>(remove).getLast()>data.you.size()) throw new Exception();

            int discarded = 0;
            for (int i: new ArrayList<>(remove).reversed()){
                data.discardpile.add(data.you.getHand().remove(i-1));
                discarded++;
            }
            data.you.drawXFrom(data.dck, discarded);

        } catch (Exception e) {
            System.out.println("Invalid discard attempt.");
            return;
        }
        data.discards--;
    }

    public static void useItem(SaveData data, Scanner inp, Random rdm){
        int pos = 1;
        if (data.items.isEmpty()){
            System.out.println("You currently have no items.");
            return;
        }
        System.out.println("Choose an item to use:");
        for (Item i: data.items){
            System.out.printf("%d: %s%n", pos, i);
            pos++;
        }
        System.out.print("\n0: Return to actions.\n");
        int use = 0;
        try{
            use = inp.nextInt();
            if (use == 0) return;
            switch(data.items.get(use-1).getName()){
                case "Broken Magnifier":
                    System.out.println("Select the position of the card you wish to see (your opponent has " + data.current.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        System.out.println("The opponent has " + selectCard(data.current, view) + " in their hand.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "+1 Uno Card":
                    data.you.drawFrom(data.dck);
                    System.out.println("Drew an extra card.");
                    break;
                case "One For @Everyone":
                    data.you.drawFrom(data.dck);
                    data.current.drawFrom(data.dck);
                    data.community.drawFrom(data.dck);
                    System.out.println("Everyone drew an extra card.");
                    break;
                case "No more middle":
                    System.out.println("Select the position of the card you wish to discard (there are " + data.community.size() + " community cards):");
                    try{
                        int view = inp.nextInt();
                        data.discardpile.add(data.community.getHand().remove(view-1));
                        System.out.println("Card has been removed.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Adoption Paper":
                    data.community.drawFrom(data.dck);
                    System.out.println("Additional card drawn to the table.");
                    break;
                case "Cursed Scroll":
                    data.current.changeChips(-data.current.getBaseChips()/10);
                    if (data.current.getCurrentChips() == 0) data.current.changeChips(1);
                    System.out.println("Enemy took 10% of max chips.");
                    break;
                case "Mr Krabz's Greed":
                    data.you.getHand().add(new Card(2, "Diamonds"));
                    data.you.getHand().add(new Card(2, "Diamonds"));
                    System.out.println("Created 2 Two of Diamonds.");
                    break;
                case "Doppleganger":
                    System.out.println("Select the position of the card you wish to duplicate (you have " + data.you.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        data.dck.add_card(selectCard(data.you, view));
                        data.you.drawFrom(data.dck);
                        System.out.println("Card duplicated.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Kings of King":
                    for (Card i: data.community.getHand()){
                        if (i.getRank() == 11 || i.getRank() == 12){
                            i.setRank(13);
                            i.updateName();
                        }
                    }
                    System.out.println("All community face cards are now Kings.");
                    break;
                case "Alu-card's Battlepass":
                    data.you.changeVampbuff(1);
                    System.out.println("Your next attack now heals you.");
                    break;
                case "I lose this, you lose that":
                    System.out.println("Select the position of the card you wish to remove (you have " + data.you.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        data.discardpile.add(data.you.discard(selectCard(data.you, view)));
                        data.discardpile.add(data.current.discard(selectCard(data.current, rdm)));
                        System.out.println("Cards from both player discarded.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "CSync":
                    System.out.println("Select the position of the card you wish to copy suit from (you have " + data.you.size() + " cards in hand and there are " + data.community.size() + " community cards):");
                    try{
                        int view = inp.nextInt();
                        String suittocopy;
                        if (view > data.you.size()) suittocopy = selectCard(data.community, view-data.you.size()).getSuit();
                        else suittocopy = selectCard(data.you, view).getSuit();
                        for (Card c: data.you.getHand()) c.setSuit(suittocopy);
                        System.out.println("Hand's suits changed.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Demotion":
                    System.out.println("Select the position of the card you wish to lower rank (you have " + data.you.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        selectCard(data.you, view).changeRank(-1);
                        System.out.println("Rank of card lowered.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Strength":
                    System.out.println("Select the position of the card you wish to raise rank (you have " + data.you.size() + " cards in hand):");
                    try{
                        int view = inp.nextInt();
                        selectCard(data.you, view).changeRank(1);
                        System.out.println("Rank of card raised.");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "Pile of cash":
                    data.you.changeChips(data.you.getBaseChips()/5);
                    System.out.println("Regained 20% of max chips.");
                    break;
                case "Madness Potion":
                    data.you.changeBlindbuff(3);
                    System.out.println("You will now nullify the next 3 turns worth of blind damage.");
                    break;
                case "Slate of *null*":
                    data.dck.peek().setWild(true);
                    data.you.drawFrom(data.dck);
                    System.out.println("Made and drew a wild card.");
                    break;
                case "Fairy's tooth":
                    data.you.changeChips(-data.you.getBaseChips()/10);
                    data.you.drawXFrom(data.dck, 3);
                    System.out.println("Drew 3 extra cards in exchange for " + (data.you.getBaseChips()/10) + " chips.");
                    break;
                case "Rainbow":
                    boolean ok = true;
                    int drawcount = 0;
                    Set<String> unique = new TreeSet<>();
                    for (Card i: data.you.getHand()){
                        if (!unique.contains(i.getSuit())&&!i.isWild()) unique.add(i.getSuit());
                        else{
                            ok = false;
                            break;
                        }
                    }
                    while(ok){
                        data.you.drawFrom(data.dck);
                        drawcount++;
                        if (!unique.contains(data.you.getHand().getLast().getSuit())&&!data.you.getHand().getLast().isWild()) unique.add(data.you.getHand().getLast().getSuit());
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
        data.items.remove(use-1);
    }

    public static Card selectCard(Person a, int pos){
        return a.getHand().get(pos-1);
    }

    public static Card selectCard(Person a, Random rdm){
        return a.getHand().get(rdm.nextInt(a.size()));
    }

    public static void finishTurn(HandValue hv, HandValue hv2, SaveData data){
        Combat.resolveRound(data.you, hv, data.current, hv2, data.state);
        // discards all cards
        while (!data.you.hasNoCards()) data.dck.add_card(data.you.discardTop());
        while (!data.current.hasNoCards()) data.dck.add_card(data.current.discardTop());
        while (!data.community.hasNoCards()) data.dck.add_card(data.community.discardTop());
        while (!data.discardpile.isEmpty()) data.dck.add_card(data.discardpile.removeFirst());
        data.dck.shuffle();
        if (hv.compareTo(hv2) < 0 || data.state.equals("Fold")) data.discards = 2;
        if (data.current.getCurrentChips() == 0){
            finishRound(data);
            return;
        }
        data.current.uniqueAction();
    }

    public static void finishRound(SaveData data){
        System.out.printf("You won! Gained %d chips and %d items! Proceeding to next battle.\n", data.you.getBaseChips()/2, 2+data.round);
        data.you.changeChips(data.you.getBaseChips()/2);
        for (int i = 0; i<2+data.round; i++) Item.itemGen(data.items, "Random");
        data.turn = 0;
        data.round++;
        if (data.round > data.gauntlet.length) return;
        data.current = data.gauntlet[data.round-1];
        data.dck = new Deck();
        System.out.printf("****************ROUND %d****************\n\n", data.round);
    }

    public static void startTurn(SaveData data, Scanner inp, Random rdm){
        if (!data.state.equals("Continue")){
            data.turn++;
            startOfTurn(data);
            dealCardsToPlayers(data);
        }
        playerAction(data, inp, rdm);
    }

    public static void resolveTurn(SaveData data){
        HandValue hv = calculatePlayer(data.you, data.community);
        HandValue hv2 = calculatePlayer(data.current, data.community);
        try {
            Thread.sleep(1000);  // 1.5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // initiate combat with stats
        finishTurn(hv, hv2, data);
    }

    public static void finishGame(SaveData data){
        if (data.round > data.gauntlet.length) System.out.printf("Congratulations! %s has defeated every boss and become the ultimate gambler with %d chips to spare!!!!\n", data.you.getName(), data.you.getCurrentChips());
        else System.out.printf("%s has ran out of chips in round %d... %s wins with %d chips remaining. Better luck next time!\n", data.you.getName(), data.round, data.current.getName(), data.current.getCurrentChips());
        try (FileWriter savefile = new FileWriter("Saves/game.sav", false)){
            savefile.write("NO DATA");
        }
        catch (Exception _){}
    }

}
