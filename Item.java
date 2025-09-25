public  class Item {
    private String ID, item_name, description;

    public Item(String item_name){
        this.item_name = item_name;
    }

    public String getName(){
        return item_name;
    }
    public void when_is_used(){
        System.out.println("what item?");
    }
    public void use(){
        System.out.println("what item?");
    }
}
