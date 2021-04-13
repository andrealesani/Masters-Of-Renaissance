package model;

import Exceptions.EmptyDeckException;
import Exceptions.NotEnoughResourceException;
import Exceptions.NotEnoughSpaceException;
import Exceptions.SlotNotValidException;
import model.card.DevelopmentCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTableTest {
    @Test
    void constructor() {
        CardTable cardTable = new CardTable();
    }

    /**
     * Ensures that all the decks contain only cards with the specified level and color
     */
    @Test
    void createDecksFromJSON() {
        CardTable cardTable = new CardTable();
        for (DevelopmentCard developmentCard : cardTable.getGreenCards().get(0)) {
            assertTrue(developmentCard.getLevel() == 3);
            assertTrue(developmentCard.getColor() == CardColor.GREEN);
        }
        for (DevelopmentCard developmentCard : cardTable.getGreenCards().get(1)) {
            assertTrue(developmentCard.getLevel() == 2);
            assertTrue(developmentCard.getColor() == CardColor.GREEN);
        }
        for (DevelopmentCard developmentCard : cardTable.getGreenCards().get(2)) {
            assertTrue(developmentCard.getLevel() == 1);
            assertTrue(developmentCard.getColor() == CardColor.GREEN);
        }
        for (DevelopmentCard developmentCard : cardTable.getBlueCards().get(0)) {
            assertTrue(developmentCard.getLevel() == 3);
            assertTrue(developmentCard.getColor() == CardColor.BLUE);
        }
        for (DevelopmentCard developmentCard : cardTable.getBlueCards().get(1)) {
            assertTrue(developmentCard.getLevel() == 2);
            assertTrue(developmentCard.getColor() == CardColor.BLUE);
        }
        for (DevelopmentCard developmentCard : cardTable.getBlueCards().get(2)) {
            assertTrue(developmentCard.getLevel() == 1);
            assertTrue(developmentCard.getColor() == CardColor.BLUE);
        }
        for (DevelopmentCard developmentCard : cardTable.getYellowCards().get(0)) {
            assertTrue(developmentCard.getLevel() == 3);
            assertTrue(developmentCard.getColor() == CardColor.YELLOW);
        }
        for (DevelopmentCard developmentCard : cardTable.getYellowCards().get(1)) {
            assertTrue(developmentCard.getLevel() == 2);
            assertTrue(developmentCard.getColor() == CardColor.YELLOW);
        }
        for (DevelopmentCard developmentCard : cardTable.getYellowCards().get(2)) {
            assertTrue(developmentCard.getLevel() == 1);
            assertTrue(developmentCard.getColor() == CardColor.YELLOW);
        }
        for (DevelopmentCard developmentCard : cardTable.getPurpleCards().get(0)) {
            assertTrue(developmentCard.getLevel() == 3);
            assertTrue(developmentCard.getColor() == CardColor.PURPLE);
        }
        for (DevelopmentCard developmentCard : cardTable.getPurpleCards().get(1)) {
            assertTrue(developmentCard.getLevel() == 2);
            assertTrue(developmentCard.getColor() == CardColor.PURPLE);
        }
        for (DevelopmentCard developmentCard : cardTable.getPurpleCards().get(2)) {
            assertTrue(developmentCard.getLevel() == 1);
            assertTrue(developmentCard.getColor() == CardColor.PURPLE);
        }

        // Checks that all decks have 4 cards each
        assertEquals(4, cardTable.getGreenCards().get(0).size());
        assertEquals(4, cardTable.getGreenCards().get(1).size());
        assertEquals(4, cardTable.getGreenCards().get(2).size());
        assertEquals(4, cardTable.getBlueCards().get(0).size());
        assertEquals(4, cardTable.getBlueCards().get(1).size());
        assertEquals(4, cardTable.getBlueCards().get(2).size());
        assertEquals(4, cardTable.getYellowCards().get(0).size());
        assertEquals(4, cardTable.getYellowCards().get(1).size());
        assertEquals(4, cardTable.getYellowCards().get(2).size());
        assertEquals(4, cardTable.getPurpleCards().get(0).size());
        assertEquals(4, cardTable.getPurpleCards().get(1).size());
        assertEquals(4, cardTable.getPurpleCards().get(2).size());

        cardTable.getGreenCards().get(0).get(0).getProduction();
    }

    @Test
    void discardTop() throws EmptyDeckException {
        CardTable cardtable = new CardTable();
        for (int i = 0; i < 12; i++)
            cardtable.discardTop(CardColor.YELLOW);

        Exception ex = assertThrows(EmptyDeckException.class, () -> {
            cardtable.discardTop(CardColor.YELLOW);
        });
    }

    @Test
    void buyTopCard() throws SlotNotValidException, NotEnoughResourceException {
        CardTable cardTable = new CardTable();
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null);

        cardTable.buyTopCard(CardColor.GREEN, 3, playerBoard, 1);
        assertTrue(playerBoard.getCardSlots().get(0).get(0).getColor() == CardColor.GREEN);
        assertTrue(playerBoard.getCardSlots().get(0).get(0).getLevel() == 1);
        System.out.println(playerBoard.getCardSlots().get(0).get(0).getVictoryPoints());
    }

    @Test
    void checkAllColorsAvailable() throws EmptyDeckException {
        CardTable cardTable = new CardTable();
        assertTrue(cardTable.checkAllColorsAvailable());

        for (int i = 0; i < 11; i++)
            cardTable.discardTop(CardColor.GREEN);
        assertTrue(cardTable.checkAllColorsAvailable());

        cardTable.discardTop(CardColor.GREEN);
        assertFalse(cardTable.checkAllColorsAvailable());
    }
}