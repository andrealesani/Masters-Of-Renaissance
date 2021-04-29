package network.beans;

import model.CardColor;
import model.Game;
import model.card.DevelopmentCard;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class CardTableBeanTest {

    /* The test doesn't work anymore since beans setters are now private methods :(
    @Test
    void setCardTableFromBP() {
        Game game = new Game();
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : game.getCardTable().getCards().entrySet()) {
            for (List<DevelopmentCard> deck : color.getValue()) {
                System.out.print(" " + game.getCardTable().getTopCardId(deck) + " ");
            }
            System.out.println("\n");
        }
        CardTableBean cardTableBean = new CardTableBean();
        cardTableBean.setCardTableFromGame(game.getCardTable());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + cardTableBean.getCards()[i][j] + " ");
            }
            System.out.println("\n");
        }
    }
     */
}