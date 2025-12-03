package Items;

import Game.*;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

public class Item {
    public static String[] Item_Names = {
            "+1 Uno Card","Demotion","Strength","Pile of cash"
    };
    private String item_name, description, howToUse;
    private int baseChipCost;
    private ImageIcon icon;
    private static final int DEFAULT_ICON_SIZE = 50;

    public Item(String item_name, String description, String howToUse, String iconPath, int baseChipCost){
        this.item_name = item_name;
        this.howToUse = howToUse;
        this.description = description;
        this.baseChipCost = baseChipCost;
        try {
            java.net.URL url = getClass().getResource(iconPath);
            if (url != null) {
                ImageIcon originalIcon = new ImageIcon(url);
                // Resize ảnh
                Image scaledImage = originalIcon.getImage().getScaledInstance(DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE, Image.SCALE_SMOOTH);
                this.icon = new ImageIcon(scaledImage);
            } else {
                System.err.println("Icon not found for: " + iconPath);
                this.icon = null; // Gán null nếu không tìm thấy
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + iconPath + " - " + e.getMessage());
            this.icon = null;
        }
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

    public int getBaseChipCost(){
        return baseChipCost;
    }

    public void setIcon(String iconPath) {
        this.icon = new ImageIcon(iconPath);
    }

    public String toString(){
        return String.format("%s: %s", item_name, howToUse);
    }

    public static Item itemGen(String i){
        switch(i){
            case "+1 Uno Card":
                return (new CardDraw());

            case "Demotion":
                return (new RankDec());

            case "Strength":
                return (new RankInc());

            case "Pile of cash":
                return (new RegainChips());

        }
        return null;
    }

    public boolean use(GameController controller){
        System.out.println("Items?");
        // dành cho các items con
        return true;
    }
}

