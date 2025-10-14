package Items;
import Person.Person;

public class DecreaseCardRank extends Item {
    public IncreaseCardRank(String item_name){
        super(item_name);
        this.ID = 105;
        this.howToUse = "Decrease the rank of 1 card in your hand";
        this.description = "A step back for the greater good";
    }

    @Override
    public void use(Person self, Card x){
        x.decreaseRank();
    }
}

