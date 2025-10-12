package Items;
import Person.Person;

public  class Item {
    protected String item_name, description;
    protected int ID;

    public Item(String item_name){
        this.item_name = item_name;
    }

    public String getName(){
        return item_name;
    }

    public String getDescription(){
        return description;
    }
    
    public void use(Person user, Person Target){
        System.out.println("What item?"); //same
    }

    public void use(Person self){
        use(self, self);
    }
}








