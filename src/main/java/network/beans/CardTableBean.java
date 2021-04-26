package network.beans;

import model.CardColor;
import model.Game;
import model.card.DevelopmentCard;

import java.util.List;
import java.util.Map;

public class CardTableBean {
    private final int[][] cardTable = new int[4][3];

    // GETTERS

    public int[][] getCardTable() {
        return cardTable;
    }

    // SETTERS

    public void setCardTableFromGame(Game game) {
        int i = 0, j = 0;
        for(Map.Entry<CardColor, List<List<DevelopmentCard>>> color : game.getCardTable().getCards().entrySet()) {
            j = 0;
            for(List<DevelopmentCard> deck : color.getValue()) {
                cardTable[i][j] = game.getCardTable().getTopCardId(deck);
                j++;
            }
            i++;
        }
    }
}
