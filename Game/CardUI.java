package Game;

import Cards.Card;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CardUI extends JPanel {
    private Image cardImage;

    public CardUI(Card card) {
        String path = "";
        if(card.getFaceUp()){
            path = "Cards (large)/card_" + card.getSuit().toLowerCase() + "_" + chuanHoa(card.getRank()) + ".png";
        }
        else{
            path  = "Cards (large)/card_back.png";
        }
        this.cardImage = new ImageIcon(getClass().getResource(path)).getImage();
        setPreferredSize(new Dimension(100, 150)); // scale image to fit this

        setOpaque(false);
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

        int padding = 10; // space around the card

        // original image dimensions
        int imgWidth = cardImage.getWidth(null);
        int imgHeight = cardImage.getHeight(null);

        // panel drawable area
        int panelWidth = getWidth() - padding*2;
        int panelHeight = getHeight() - padding*2;

        // scale factor to fit inside panel
        double scaleX = panelWidth / (double) imgWidth;
        double scaleY = panelHeight / (double) imgHeight;
        double scale = Math.min(scaleX, scaleY); // keep aspect ratio
        scale = Math.max(1.0, scale);

        int drawWidth = (int) (imgWidth * scale);
        int drawHeight = (int) (imgHeight * scale);

        // center the image
        int x = (getWidth() - drawWidth) / 2;
        int y = (getHeight() - drawHeight) / 2;

        g.drawImage(cardImage, x, y, drawWidth, drawHeight, this);
    }

}