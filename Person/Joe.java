package Person;

public class Joe extends Person{
    public Joe(String charName, int baseChips){
        super(charName, 1.0, 1.0, baseChips);
        super.changeCardDraw(1);
    }
}
