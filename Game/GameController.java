package Game;

import Battle.*;
import Cards.Card;
import Cards.Deck;
import Items.*;
import Person.*;
import PokerHands.Calculator;
import PokerHands.HandValue;
import Saves.*;
import java.util.*;

public class GameController {

    private final Random rdm = new Random();

    private SaveData data = Loader.load();

    private Item[] itemSlots = new Item[6];
    private List<Card> discardPile = new ArrayList<>();
    private Person community = new Person("Community", 1, 1, 1);

    private Deck deck = new Deck();
    private Player you;
    private Person currentEnemy;
    private Person[] gauntlet;

    private int discards = 2;
    private int round = 1;
    private int turn = 0;
    private String state = "";
    private String playerAction = "";

    private int wager = 0;

    private GameUI ui;

    private HandValue pHand; 
    private HandValue eHand; 
    private int winnerID = -1;


    // ========================
    // INITIALIZATION
    // ========================
    public void startNewGame(String playerName) {
        you = new Player(playerName, 1.0, 1.0, 100, 100);
        initGauntlet();
        currentEnemy = gauntlet[0];
        deck = new Deck();
        deck.shuffle();

        while (!community.hasNoCards()) {
        deck.add_card(community.discardTop());
        }
        
        turn = 1;
        discards = 2;
        dealCards();
        if (ui != null) ui.updateUI();
    }

    public void continueGame(){
        if (data!=null){
            you = data.you;
            gauntlet = data.gauntlet;
            currentEnemy = data.current;
            deck = data.dck;
            community = data.community;
            turn = data.turn;
            round = data.round;
            discards = data.discards;
            discardPile = data.discardpile;
            itemSlots = data.items;
            state = data.state;
            if (ui != null) ui.updateUI();
        }
        else{
            //bad data, start normal game (or you can make the button invisible)

        }
    }

    public void saveGame() {
        // Thu thập dữ liệu cần lưu
        SaveData data = new SaveData(
            getItemSlots(),       // items
            community,            // community
            discardPile,          // discardpile
            deck,                  // dck
            discards,             // discards
            round,                // round
            turn,                 // turn
            gauntlet,             // gauntlet
            currentEnemy,         // current (Enemy)
            you,                  // you (Player)
            state                 // state
        );
        
        // Gọi lớp Saver để thực hiện lưu file
        Saver.save(data);
        
        // Tùy chọn: Có thể thoát ứng dụng sau khi lưu
        // System.exit(0); 
    }

    private void initGauntlet() {
        gauntlet = new Person[]{
                new Person("Boss 1", 100),
                new Person("Boss 2", 150),
                new Breno("Breno", 180, 0),
                new Joe("Joe", 200)


        };
    }

    // ========================
    // CARD DEALING
    // ========================
    public void dealCards() {
        deck.shuffle(); 
        you.drawXFrom(deck, you.getInit_card_draw());
        currentEnemy.drawXFrom(deck, currentEnemy.getInit_card_draw());
        community.drawXFrom(deck, 5);
    }

    public void nextTurn() {
        turn++;
        applyBlindDamage();
        
        // Loại bỏ việc hồi HP về 1 nếu HP <= 0 (Logic đã sửa)
        wager = 0;
        dealCards();
        discards = 2;
        state = "";
        playerAction = "";

        if (ui != null) ui.updateUI();
    }

    private void applyBlindDamage() {
        if (turn > 10) {
            if (you.getBlindbuff() > 0) you.changeBlindbuff(-1);
            else you.changeChips(-(turn / 2)); 
        } else if (turn > 5) {
            if (you.getBlindbuff() > 0) you.changeBlindbuff(-1);
            else you.changeChips(-3);
        }
    }

    // ========================
    // PLAYER ACTIONS
    // ========================
    public void playerCall() {
        playerAction = "Call";
        wager = 0;
        startShowdown(); 
    }

    public boolean playerRaise() { 
        if (you.getCurrentChips() < 20) return false;
        you.changeChips(-20);
        playerAction = "Raise"; 
        wager = 20;
        startShowdown();
        return true;
    }

    public void playerFold() { 
        you.changeChips(-5);
        playerAction = "Fold"; 
        startShowdown(); }

    public boolean playerDiscard(List<Integer> positions) {
        if (discards == 0 || positions.size() < 1 || positions.size() > 2) return false;

        Collections.sort(positions, Collections.reverseOrder());
        int removed = 0;
        for (int p : positions) {
            if (p < 1 || p > you.size()) return false;
            discardPile.add(you.getHand().remove(p - 1));
            removed++;
        }
        you.drawXFrom(deck, removed);
        discards--;
        if (ui != null) ui.updateUI();
        return true;
    }

    // Thay đổi bài
    public boolean replacePlayerCard(List<Integer> positions, Card newCard) {
        if (you == null || positions.size() != 1 || newCard == null) {
            return false;
        }

        int index = positions.get(0) - 1;

        List<Card> hand = you.getHand();
        if (index < 0 || index >= hand.size()) {
            return false;
        }

        // Lấy lá cũ
        Card oldCard = hand.set(index, newCard);

        // Cho vào discard pile
        discardPile.add(oldCard);

        if (ui != null) ui.updateUI();
        return true;
    }


    private void startShowdown(){

        pHand = evaluate(you); 
        eHand = evaluate(currentEnemy);

        if (eHand.compareTo(pHand) > 0 || playerAction.equals("Fold")) {
            winnerID = 1; // Enemy thắng
        } else if (pHand.compareTo(eHand) > 0) {
            winnerID = 0; // Player thắng
        } else {
            winnerID = -1; // Hòa
        }
        state = "Reveal";
        if (ui != null) ui.updateUI();
    }
    // ========================
    // ROUND / COMBAT
    // ========================
    public void resolveCombat() {

        Combat.resolveRound(you, pHand, currentEnemy, eHand, playerAction, wager);

        if (ui != null) ui.updateUI();

        if (you.getCurrentChips() <= 0) {
            // Đã bị K.O, UI sẽ xử lý Game Over
        }

        // discard tất cả bài vào deck
        while (!you.hasNoCards()) deck.add_card(you.discardTop());
        while (!currentEnemy.hasNoCards()) deck.add_card(currentEnemy.discardTop());
        while (!community.hasNoCards()) deck.add_card(community.discardTop());
        while (!discardPile.isEmpty()) deck.add_card(discardPile.remove(0));
        deck.shuffle();

        // reset discards nếu thua
        if (pHand.compareTo(eHand) < 0 || state.equals("Fold")) discards = 2;

        // Xử lý chuyển lượt/chuyển kẻ thù (Logic đã sửa)
        if (currentEnemy.getCurrentChips() <= 0) {
            if (round < gauntlet.length){
                you.setBaseChips(getYou().getBaseChips() + 20);
                state = "Shop";
                if (ui != null) {
                    // Hiển thị shop, truyền vào Item ngẫu nhiên
                    ui.showShopScreen(ItemGenerator.generateRandomItems(3));
                }
            }
            else{
                return;
            }
        } 
        else {
             nextTurn();
        }
        playerAction = "";
    }

    public void nextEnemy() {
        round++;
        if (round > gauntlet.length) return; // hết gauntlet (Game Win)

        // hồi HP player
        you.changeChips(you.getBaseChips() - you.getCurrentChips());

        // set enemy mới
        currentEnemy = gauntlet[round - 1];
        currentEnemy.changeChips(currentEnemy.getBaseChips() - currentEnemy.getCurrentChips());

        turn = 1;
        discards = 2;
        deck = new Deck(); 
        deck.shuffle();    
        dealCards(); 
        state = "";
    }

    
    // ========================
    // EVALUATION
    // ========================
    public HandValue evaluate(Person p) {
        List<Card> cards = new ArrayList<>(p.getHand());
        cards.addAll(community.getHand());
        return Calculator.evaluateAuto(cards);
    }

    // ========================
    // GETTERS (Đã bổ sung các getters cần thiết cho GameUI)
    // ========================
    public List<Card> getPlayerCards() { return you.getHand(); }
    public List<Card> getEnemyCards() { return currentEnemy.getHand(); }
    public List<Card> getCommunityCards() { return community.getHand(); }

    public int getPlayerHP() { return you.getCurrentChips(); }
    public int getPlayerMaxHP() { return you.getBaseChips(); }
    public Item[] getItemSlots() { return itemSlots; }
    public int getEnemyHP() { return currentEnemy.getCurrentChips(); }
    public int getEnemyMaxHP() { return currentEnemy.getBaseChips(); }

    public int getTurn() { return turn; }
    public int getRound() { return round; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    // Getter for save data
    public boolean hasData() { return data != null;}

    
    // Getter mới cho UI
    public int getDiscards() { return discards; }
    public Person getCurrentEnemy() { return currentEnemy; }
    public int getGauntletLength() { return gauntlet.length; } // Cho logic kiểm tra Game Win
    
    public Player getYou() { return you; }

    //Getter cho Hand
    public HandValue getPlayerBestHand() {
        return evaluate(you);
    }   

    public HandValue getEnemyBestHand() {
        return evaluate(currentEnemy);
    }

    //Getter mới cho info Panel
    public int getPlayerAttack() {
        HandValue pHand = getPlayerBestHand();
        // Giả sử Player.getTable().getTypeAtk() lấy giá trị ATK cơ bản từ loại bài
        int baseAtk = you.getTable().getTypeAtk(pHand.gethandType());
        int finalAtk = (int)(baseAtk * you.getAtkMult());
        if (state.equals("Reveal") && pHand.compareTo(eHand) > 0 && playerAction.equals("Raise")) {
            finalAtk = (int)(finalAtk * 1.5);
        }
        return finalAtk;
    }

    public int getPlayerDefense() {
        HandValue pHand = getPlayerBestHand();
        // Giả sử Player.getTable().getTypeDef() lấy giá trị DEF cơ bản từ loại bài
        int baseDef = you.getTable().getTypeDef(pHand.gethandType());
        int finalDef = (int)(baseDef * you.getDefMult());
        if (playerAction.equals("Fold")) {
            finalDef = finalDef * 3;
        }
        return finalDef;
    }

    public int getEnemyAttack() {
        HandValue eHand = getEnemyBestHand();
        int baseAtk = currentEnemy.getTable().getTypeAtk(eHand.gethandType());
        return (int)(baseAtk * currentEnemy.getAtkMult());
    }

    public int getEnemyDefense() {
        HandValue eHand = getEnemyBestHand();
        int baseDef = currentEnemy.getTable().getTypeDef(eHand.gethandType());
        return (int)(baseDef * currentEnemy.getDefMult());
    }

    // Getter cho Showcase
    public boolean isShowcaseActive() {
        return state.equals("Reveal");
    }

    // Getter cho người thắng (Dùng để tô màu)
    public int getWinnerID() {
        if (state.equals("Reveal")) {
            return winnerID;
        }
    return -1;
    }

    // Setter cho GameUI
    public void setUI(GameUI ui) {
        this.ui = ui;
    }

    public GameUI getUI(){
        return this.ui;
    }

    public int getWager() { return wager; }

    public Deck getDeck(){
        return this.deck;
    }

    public String getPlayerAction(){
        return playerAction;
    }
}