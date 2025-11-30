package Person;

public class Breno extends Person{
    public Breno(String charName, int baseChips){
        super(charName, 1.2, 1, baseChips);
    }

    public Breno(String charName, int baseChips, int rage){
        super(charName, 1.2+(0.1*rage), Math.max(1-(0.2*rage), 0), baseChips);
    }

    @Override
    public void uniqueAction() {
        changeAtk(0.1);
        changeDef(-0.2);
        System.out.println("Breno slowly builds into a frenzy. Became more aggressive and vulnerable.");
    }

    public void uniqueAction(int turn ) {
        changeAtk(0.1 * turn);
        changeDef(-0.2 * turn);
        System.out.println("Breno slowly builds into a frenzy. Became more aggressive and vulnerable.");
    }
}
