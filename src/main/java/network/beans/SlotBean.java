package network.beans;

import model.PlayerBoard;
import model.card.DevelopmentCard;

import java.util.Arrays;

/**
 * This class represent a "special" bean. This means that it doesn't have a direct class to get the data from, but it's
 * just a container used by the PlayerBoardBean to store information about its card slots. Hence, the class does not
 * implement Observer interface and its setter is public so that it can be called by PlayerBoardBean when it updated itself
 */
public class SlotBean {
    /**
     * Holds the IDs of the cards in the slot this bean represents
     */
    private int[] developmentCards;

    // GETTERS

    public int[] getDevelopmentCards() {
        return developmentCards;
    }

    // SETTERS

    public void setDevelopmentCardsFromPB(PlayerBoard playerBoard, int slot) {
        int i = 0;
        developmentCards = new int[playerBoard.getCardSlots().get(slot-1).size()];
        for(DevelopmentCard card : playerBoard.getCardSlots().get(slot - 1)) {
           developmentCards[i++] = card.getId();
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(developmentCards) +
                '\n';
    }
}
