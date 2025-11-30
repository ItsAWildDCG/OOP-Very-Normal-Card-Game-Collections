package Game;

import Cards.Card;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CardUI extends JPanel {
    private Image cardImage;
    private Card card;

    public CardUI(Card card) {
        this.card = card;
        String path = "";
        path = "Cards (large)/card_" + card.getSuit().toLowerCase() + "_" + chuanHoa(card.getRank()) + ".png";
        this.cardImage = new ImageIcon(getClass().getResource(path)).getImage();
        setPreferredSize(new Dimension(85, 120)); // scale image to fit this

        setOpaque(false);
    }

    public CardUI(Card card, boolean isHidden) {
        this.card = card;
        String path = "";
        if(!isHidden){
            path = "Cards (large)/card_" + card.getSuit().toLowerCase() + "_" + chuanHoa(card.getRank()) + ".png";
        }
        else{
            path  = "Cards (large)/card_back.png";
        }
        this.cardImage = new ImageIcon(getClass().getResource(path)).getImage();
        setPreferredSize(new Dimension(85, 120)); // scale image to fit this

        setOpaque(false);
    }

    public Card getCard(){
        return card;
    }

    public String chuanHoa(int rank){
        if (rank == 1 || rank == 14){
            return "A";
        }
        else if (rank == 11){
            return "J";
        }
        else if (rank == 12){
            return "Q";
        }
        else if (rank == 13){
            return "K";
        }
        else{
            return String.format("%02d", rank);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // original image dimensions
        int imgWidth = cardImage.getWidth(null);
        int imgHeight = cardImage.getHeight(null);

        // panel drawable area
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // scale factor to fit inside panel
        double scaleX = panelWidth / (double) imgWidth;
        double scaleY = panelHeight / (double) imgHeight;
        double scale = Math.min(scaleX, scaleY); // keep aspect ratio
        

        int drawWidth = getWidth();
        int drawHeight = getHeight();

        // center the image
        int x = 0;
        int y = 0;

        g.drawImage(cardImage, x, y, drawWidth, drawHeight, this);
    }

}