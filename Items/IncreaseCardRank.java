package Items;
import Person.Person;

public class IncreaseCardRank extends Item {
    public IncreaseCardRank(String item_name){
        super(item_name);
        this.ID = 104;
        this.howToUse = "Increase the rank of 1 card in your hand";
        this.description = "We ascend";
    }

    @Override
    public void use(Person self, Card x){
        x.increaseRank();
    }
}

