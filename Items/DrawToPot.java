package Items;
import Person.Person;
import Decks.Deck;

public class DrawToPot extends Item {
    public DrawToPot(String item_name){
        super(item_name);
        ID = 102;
        this.howToUse = "Draw 1 card from the deck to the shared pot";
        this.description = "The house add in the excitement";
    }

    @Override
    public void use(Person pot, Deck toDraw){
        pot.drawFrom(toDraw);
    }
}

