package Items;

import javax.swing.ImageIcon;

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
}

