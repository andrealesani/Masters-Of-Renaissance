package network.beans;

import model.CardColor;
import model.Game;
import model.card.DevelopmentCard;

import java.util.List;
import java.util.Map;

public class CardTableBean {
    /**
     * Holds the IDs of the cards in CardTable's cards attribute
     */
    private int[][] cards;

    // GETTERS

    public int[][] getCards() {
        return cards;
    }

    // SETTERS

    public void setCardTableFromGame(Game game) {
        cards = new int[game.getCardTable().getCards().entrySet().size()][3];
        int i = 0, j;
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : game.getCardTable().getCards().entrySet()) {
            j = 0;
            for (List<DevelopmentCard> deck : color.getValue()) {
                cards[i][j] = game.getCardTable().getTopCardId(deck);
                j++;
            }
            i++;
        }
    }
}
