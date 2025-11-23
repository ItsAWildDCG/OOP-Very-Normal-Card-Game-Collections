package Items;

import javax.swing.ImageIcon;

public  class Item {
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

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(String iconPath) {
        this.icon = new ImageIcon(iconPath);
    }
}



class RegainChips extends Item {
    public RegainChips(){
        super("Pile of cash",
                "Take my money b*tch",
                "Regain 20% of your starting chips",
                "sth sth path");
    }
}
