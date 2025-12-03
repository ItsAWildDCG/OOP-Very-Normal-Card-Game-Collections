package Items;

import Cards.*;
import Game.CardUI;
import Game.GameController;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class RankInc extends Item{
    public RankInc(){
        super("Strength",
                "Testosterone or ostrogen increase",
                "Increase rank of 1 card in your hand by 1",
                "/res/image/RankInc.png",
                3);
    }

    @Override
    public boolean use(GameController controller) {

        List<CardUI> selectedCards = new ArrayList<>(controller.getUI().selectedCards);

        if (selectedCards.isEmpty() || selectedCards.size() > 1) {
            JOptionPane.showMessageDialog(controller.getUI(), "Vui lòng chọn 1 lá bài để thay đổi");
            return false;
        }

        // Tìm vị trí lá bài (1-indexed)
        List<Integer> positions = new ArrayList<>();
        List<Card> playerCards = controller.getPlayerCards();

        Card oldCard = null;

        for (CardUI cUI : selectedCards) {
            int index = playerCards.indexOf(cUI.getCard());
            if (index != -1) {
                positions.add(index);
                oldCard = playerCards.get(index);   // LẤY LÁ BÀI CŨ
            }
        }

        if (oldCard == null) {
            JOptionPane.showMessageDialog(controller.getUI(), "Không tìm thấy lá bài trong tay.");
            return false;
        }

        Card newCard = new Card(
            oldCard.getRank() + 1,   
            oldCard.getSuit()        
        );


        if (controller.replacePlayerCard(positions, newCard)) {
            controller.getUI().updateCardRows();
            controller.getUI().updateInfoLabel();
            controller.getUI().updateUI();
        } else {
            JOptionPane.showMessageDialog(controller.getUI(), 
                "Không thể thay đổi lá bài.");
            return false;
        }
        return true;
    }
}
