package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.card.DevelopmentCard;

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

    // PRINTING METHODS

    /**
     * Prints a String representation of the bean's data
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        String result = "";

        for (int i = 0; i < developmentCards.length; i++)
            result += "lv" + (i+1) + ".[" + developmentCards[i] + "] ";

        return  result;
    }

    // GETTERS

    /**
     * Getter for the ids of the development cards stored in the card slot
     *
     * @return an array of int of the cards' id
     */
    public int[] getDevelopmentCards() {
        return developmentCards.clone();
    }

    // SETTERS

    /**
     * Sets the slot's stored cards
     *
     * @param playerBoard the object to take the information from
     * @param slot the card slot to copy
     */
    public void setDevelopmentCardsFromPB(PlayerBoard playerBoard, int slot) {
        int i = 0;
        developmentCards = new int[playerBoard.getCardSlots().get(slot-1).size()];
        for(DevelopmentCard card : playerBoard.getCardSlots().get(slot - 1)) {
            developmentCards[i++] = card.getId();
        }
    }
}
