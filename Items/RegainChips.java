package Items;
import java.util.*;
import java.math.*;
import Person.Person;
public class RegainChips extends Item {
    public RegainChips(String item_name){
        super(item_name);
        ID = 1;
        description = "A blessing from the Lord";
    }

    @Override
    public void use(Person self){
        self.healed(Math.round(baseChips / 5));
    }
}   
