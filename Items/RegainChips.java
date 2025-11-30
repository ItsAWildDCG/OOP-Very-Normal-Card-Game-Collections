package Items;

import Game.GameController;

public class RegainChips extends Item {
    public RegainChips(){
        super("Pile of cash",
                "Take my money b*tch",
                "Regain 20% of your starting chips",
                "/res/image/RegainChips.png",
                5);
    }
    @Override
    public void use(GameController controller) {
        controller.getYou().changeChips((int)(controller.getYou().getBaseChips() * 20 / 100)); 
        if (controller.getUI() != null) controller.getUI().updateUI(); // Cập nhật thanh máu
    }
}
