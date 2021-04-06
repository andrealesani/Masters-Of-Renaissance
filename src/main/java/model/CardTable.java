package model;

import model.card.DevelopmentCard;

import java.util.List;

public class CardTable {
    private List<List<DevelopmentCard>> greenCards;
    private List<List<DevelopmentCard>> blueCards;
    private List<List<DevelopmentCard>> yellowCards;
    private List<List<DevelopmentCard>> purpleCards;

    /**
     * Adds the selected card to the specified PlayerBoard in the specified slot
     *
     * @param column      specifies the column of the card in the grid
     * @param row         specifies the row of the card in the grid
     * @param playerBoard specifies which player is buying the card
     * @param cardSlot    specifies in which production slot the player wants to put the card
     */
    public void buyTopCard(CardColor column, int row, PlayerBoard playerBoard, int cardSlot) {
        List<List<DevelopmentCard>> deckColumn = null;
        if (column.equals(CardColor.GREEN))
            deckColumn = greenCards;
        else if (column.equals(CardColor.BLUE))
            deckColumn = blueCards;
        else if (column.equals(CardColor.YELLOW))
            deckColumn = yellowCards;
        else if (column.equals(CardColor.PURPLE))
            deckColumn = purpleCards;

        playerBoard.addDevelopmentCard(cardSlot, deckColumn.get(row).get(0));
    }

    public void discardTop(CardColor color, int idk) {
        //TODO
    }

    /**
     * Checks that there's at least one card of each color still available (this method should be used only in solo mode)
     *
     * @return true if there's at least one card of each color still available
     */
    public boolean checkAllColorAvailable() {
        for (List<DevelopmentCard> deck : greenCards) {
            if (deck.size() < 1)
                return false;
        }
        for (List<DevelopmentCard> deck : blueCards) {
            if (deck.size() < 1)
                return false;
        }
        for (List<DevelopmentCard> deck : yellowCards) {
            if (deck.size() < 1)
                return false;
        }
        for (List<DevelopmentCard> deck : purpleCards) {
            if (deck.size() < 1)
                return false;
        }

        return true;
    }
}
