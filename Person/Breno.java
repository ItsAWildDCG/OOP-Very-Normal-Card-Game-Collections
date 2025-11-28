package Person;

public class Breno extends Person{
    public Breno(String charName, int baseChips){
        super(charName, 1.2, 1, baseChips);
    }

    public Breno(String charName, int baseChips, int rage){
        super(charName, 1.2+(0.05*rage), Math.max(1-(0.05*rage), 0), baseChips);
    }

    @Override
    public void uniqueAction() {
        changeAtk(0.05);
        changeDef(-0.05);
        System.out.println("Breno slowly builds into a frenzy. Became more aggressive and vulnerable.");
    }
}
