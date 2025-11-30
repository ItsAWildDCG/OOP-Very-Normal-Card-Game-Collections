package Items;

import Game.GameController;

public class CardDraw extends Item {
    public CardDraw(){
        super("+1 Uno Card",
                "Normally it's bad but now it's good",
                "Draw 1 card from the deck to your hand",
                "/res/image/+!UNO.png",
                6);
    }

    @Override
    public void use(GameController controller) {
        controller.getYou().drawXFrom(controller.getDeck(), 1); 
        if (controller.getUI() != null) controller.getUI().updateUI();
    }
}
