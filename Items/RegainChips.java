package Items;
import java.util.*;
import java.math.*;
import Person.Person;
public class RegainChips extends Item {
    public RegainChips(String item_name){
        super(item_name);
        this.ID = 109;
        this.howToUse = "Regain 20% of your starting chips";
        this.description = "A blessing from the Lord";
    }

    @Override
    public void use(Person self){
        self.healed(Math.round(self.getBaseChips() / 5));
    }
}   



