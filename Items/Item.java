package Items;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.Random;

public class Item {
    public static String[] Item_Names = {
            "Broken Magnifier","+1 Uno Card","One For @Everyone","No more middle","Adoption Paper","Cursed Scroll","Mr Krabz's Greed","Doppleganger","Kings of King",
            "Alu-card's Battlepass","I lose this, you lose that","CSync","Demotion","Strength","Pile of cash","Madness Potion","Slate of *null*","Fairy's tooth","Rainbow"
    };
    private String item_name, description, howToUse;
    private ImageIcon icon;

    public Item(String item_name, String description, String howToUse, String iconPath){
        this.item_name = item_name;
        this.howToUse = howToUse;
        this.description = description;
        this.icon = new ImageIcon(iconPath);
    }

    public String getName() {
        return item_name;
    }

    public String getDescription() {
        return description;
    }

    public String getHowToUse() {
        return howToUse;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(String iconPath) {
        this.icon = new ImageIcon(iconPath);
    }

    public String toString(){
        return String.format("%s: %s", item_name, howToUse);
    }

    public static void itemGen(ArrayList<Item> items, String i){
        String nextItem;
        Random rdm = new Random();
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
}

