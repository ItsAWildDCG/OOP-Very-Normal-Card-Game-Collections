package Items;
import Person.Person;

public class DamageDealer extends Item {
    public IncreaseCardRank(String item_name){
        super(item_name);
        this.ID = 107;
        this.howToUse = "Deal 10 damage to the dealer";
        this.description = "Take this";
    }

    @Override
    public void use(Person self, Person target){
        target.takeDamage(10);
    }
}
