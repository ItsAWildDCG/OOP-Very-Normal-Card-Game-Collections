package Items;
import Person.Person;
import Decks.Deck;

public class DrawToHand extends Item {
    public DrawToHand(String item_name){
        super(item_name);
        this.ID = 101;
        this.howToUse = "Draw 1 card from the deck to your hand";
        this.description = "Sneaky hand does the work";
    }

    public void use(Person self, Deck toDraw){
        self.drawFrom(toDraw);
    }
}
