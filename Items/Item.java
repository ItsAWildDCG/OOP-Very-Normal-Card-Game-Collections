package Items;

public  class Item {
    private String ID, item_name, description;

    public Item(String item_name){
        this.item_name = item_name;
    }

    public String getName(){
        return item_name;
    }

    public String getDescription(){
        return description;
    }
    
    public void when_is_used(){
        System.out.println("When"); // will be changed later depend on the item
    }
    
    public void use(){
        System.out.println("What item?"); //same
    }
}


