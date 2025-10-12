package Items;
import Person.Person;

public class RemoveFromPot extends Item {
    public RemoveFromPot(String item_name){
        super(item_name);
        this.ID = 103;
        this.howToUse = "Remove a card from the shared pot";
        this.description = "We go down together";
    }

    @Override
    public void use(Person pot){
        pot.discardTop();
    }
}
