package network.beans;

import model.CardColor;
import model.Game;
import model.card.DevelopmentCard;

import java.util.List;
import java.util.Map;

public class CardTableBean {
    private int[][] cardTable;

    // GETTERS

    public int[][] getCardTable() {
        return cardTable;
    }

    // SETTERS

    public void setCardTableFromGame(Game game) {
        cardTable = new int[game.getCardTable().getCards().entrySet().size()][3];
        int i = 0, j;
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : game.getCardTable().getCards().entrySet()) {
            j = 0;
            for (List<DevelopmentCard> deck : color.getValue()) {
                cardTable[i][j] = game.getCardTable().getTopCardId(deck);
                j++;
            }
            i++;
        }
    }
}
