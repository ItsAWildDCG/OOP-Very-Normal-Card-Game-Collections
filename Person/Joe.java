package Person;

public class Joe extends Person{
    public Joe(String charName, int baseChips){
        super(charName, 0.9, 0.9, baseChips);
        super.changeCardDraw(1);
    }
}
