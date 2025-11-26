package Person;

public class Player extends Person{
    public Player(String charName, double atkMult, double defMult, int baseChips) {
        super(charName, atkMult, defMult, baseChips);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }


}
