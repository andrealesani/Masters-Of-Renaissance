package network.beans;

import model.PlayerBoard;
import model.card.DevelopmentCard;

import java.util.Arrays;

public class SlotBean {
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
        return "SlotBean{" +
                "\ndevelopmentCards=" + Arrays.toString(developmentCards) +
                '\n' + '}';
    }
}
