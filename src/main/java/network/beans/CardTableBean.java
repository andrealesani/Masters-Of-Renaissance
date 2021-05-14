package network.beans;

import com.google.gson.Gson;
import model.*;
import model.card.DevelopmentCard;
import network.GameController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CardTableBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * Holds the IDs of the cards in CardTable's cards attribute
     */
    private int[][] cards;

    // CONSTRUCTOR

    public CardTableBean(GameController controller) {
        this.controller = controller;
    }


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

        controller.broadcastMessage(MessageType.CARDTABLE, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.CARDTABLE, gson.toJson(this));
    }

    @Override
    public String toString() {
        String board = "";
        for(int[] row : cards) {
            board += "\n   ";
            for (int cell : row) {
                board += "\033[48;2;0;51;51m  " + cell + "  \u001B[0m ";
            }
            board += "\n";
        }
        return "\u001B[32;1mCardTable:\u001B[0m\n" +
                board;
    }
}
