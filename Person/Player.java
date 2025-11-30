package Person;

public class Player extends Person{
    private int vampbuff = 0;
    private int blindbuff = 0;

    public Player(String charName, double atkMult, double defMult, int baseChips) {
        super(charName, atkMult, defMult, baseChips);
    }

    public Player(String charName, double atkMult, double defMult, int currentChips, int baseChips) {
        super(charName, atkMult, defMult, currentChips ,baseChips);
    }

    public int getBlindbuff() {
        return blindbuff;
    }

    public int getVampbuff() {
        return vampbuff;
    }

    public void changeVampbuff(int vampbuff) {
        this.vampbuff += vampbuff;
    }

    public void changeBlindbuff(int blindbuff) {
        this.blindbuff += blindbuff;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }


}
