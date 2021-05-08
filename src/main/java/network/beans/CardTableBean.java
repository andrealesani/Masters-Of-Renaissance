package network.beans;

import com.google.gson.Gson;
import model.CardColor;
import model.CardTable;
import model.Observer;
import model.card.DevelopmentCard;

import java.util.List;
import java.util.Map;

public class CardTableBean implements Observer {
    /**
     * Holds the IDs of the cards in CardTable's cards attribute
     */
    private int[][] cards;

    // GETTERS

    public int[][] getCards() {
        return cards;
    }

    // SETTERS

    private void setCardTableFromGame(CardTable cardTable) {
        cards = new int[cardTable.getCards().entrySet().size()][3];
        int i = 0, j;
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : cardTable.getCards().entrySet()) {
            j = 0;
            for (List<DevelopmentCard> deck : color.getValue()) {
                cards[i][j] = cardTable.getTopCardId(deck);
                j++;
            }
            i++;
        }
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        CardTable cardTable = (CardTable) observable;
        setCardTableFromGame(cardTable);

        MessageWrapper messageWrapper = new MessageWrapper(MessageType.CARDTABLE, gson.toJson(this));

        // TODO ask to the Controller to be sent to the clients
    }
}
