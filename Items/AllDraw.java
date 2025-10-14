package Items;
import Person.Person;

public class AllDraw extends Item {
    public AllDraw(String item_name){
        super(item_name);
        this.ID = 202;
        this.howToUse = "Draw a card from the deck to your hand, to the dealer and to the shared pot";
        this.description = "Sharing is caring";
    }

    @Override
    public void use(Person self, Person Dealer, Person pot, Deck toDraw){
        self.drawFrom(toDraw);
        Dealer.drawFrom(toDraw);
        pot.drawFrom(toDraw);
    }
}
