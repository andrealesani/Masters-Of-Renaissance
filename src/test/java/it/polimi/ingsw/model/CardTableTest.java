package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.EmptyDeckException;
import it.polimi.ingsw.Exceptions.NotEnoughResourceException;
import it.polimi.ingsw.Exceptions.SlotNotValidException;
import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.storage.UnlimitedStorage;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CardTableTest {

    /**
     * Ensures that all the decks contain only cards with the specified level and color
     */
    @Test
    void createDecksFromJSON() {
        CardTable cardTable = new CardTable(0);
        for (DevelopmentCard developmentCard : cardTable.getGreenCards().get(2)) {
            assertTrue(developmentCard.getLevel() == 3);
            assertTrue(developmentCard.getColor() == CardColor.GREEN);
        }
        for (DevelopmentCard developmentCard : cardTable.getGreenCards().get(1)) {
            assertTrue(developmentCard.getLevel() == 2);
            assertTrue(developmentCard.getColor() == CardColor.GREEN);
        }
        for (DevelopmentCard developmentCard : cardTable.getGreenCards().get(0)) {
            assertTrue(developmentCard.getLevel() == 1);
            assertTrue(developmentCard.getColor() == CardColor.GREEN);
        }
        for (DevelopmentCard developmentCard : cardTable.getBlueCards().get(2)) {
            assertTrue(developmentCard.getLevel() == 3);
            assertTrue(developmentCard.getColor() == CardColor.BLUE);
        }
        for (DevelopmentCard developmentCard : cardTable.getBlueCards().get(1)) {
            assertTrue(developmentCard.getLevel() == 2);
            assertTrue(developmentCard.getColor() == CardColor.BLUE);
        }
        for (DevelopmentCard developmentCard : cardTable.getBlueCards().get(0)) {
            assertTrue(developmentCard.getLevel() == 1);
            assertTrue(developmentCard.getColor() == CardColor.BLUE);
        }
        for (DevelopmentCard developmentCard : cardTable.getYellowCards().get(2)) {
            assertTrue(developmentCard.getLevel() == 3);
            assertTrue(developmentCard.getColor() == CardColor.YELLOW);
        }
        for (DevelopmentCard developmentCard : cardTable.getYellowCards().get(1)) {
            assertTrue(developmentCard.getLevel() == 2);
            assertTrue(developmentCard.getColor() == CardColor.YELLOW);
        }
        for (DevelopmentCard developmentCard : cardTable.getYellowCards().get(0)) {
            assertTrue(developmentCard.getLevel() == 1);
            assertTrue(developmentCard.getColor() == CardColor.YELLOW);
        }
        for (DevelopmentCard developmentCard : cardTable.getPurpleCards().get(2)) {
            assertTrue(developmentCard.getLevel() == 3);
            assertTrue(developmentCard.getColor() == CardColor.PURPLE);
        }
        for (DevelopmentCard developmentCard : cardTable.getPurpleCards().get(1)) {
            assertTrue(developmentCard.getLevel() == 2);
            assertTrue(developmentCard.getColor() == CardColor.PURPLE);
        }
        for (DevelopmentCard developmentCard : cardTable.getPurpleCards().get(0)) {
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

    /**
     * Tests the discarding of cards from the card table
     */
    @Test
    void discardTop() throws EmptyDeckException {
        CardTable cardtable = new CardTable(0);
        for (int i = 0; i < 12; i++)
            cardtable.discardTop(CardColor.YELLOW);

        Exception ex = assertThrows(EmptyDeckException.class, () -> {
            cardtable.discardTop(CardColor.YELLOW);
        });
    }

    /**
     * Tests the acquisition of the card on top of a deck
     */
    @Test
    void buyTopCard() throws SlotNotValidException, NotEnoughResourceException, EmptyDeckException {
        CardTable cardTable = new CardTable(0);
        PlayerBoard playerBoard = new PlayerBoard();
        UnlimitedStorage strongbox = playerBoard.getStrongbox();
        strongbox.addResources(Map.of(ResourceType.SHIELD, 20));
        strongbox.addResources(Map.of(ResourceType.COIN, 20));
        strongbox.addResources(Map.of(ResourceType.STONE, 20));
        strongbox.addResources(Map.of(ResourceType.SERVANT, 20));

        cardTable.buyTopCard(CardColor.GREEN, 1, playerBoard, 1);
        assertTrue(playerBoard.getCardSlots().get(0).get(0).getColor() == CardColor.GREEN);
        assertTrue(playerBoard.getCardSlots().get(0).get(0).getLevel() == 1);
    }

    /**
     * Tests the method which checks if there is still at least one card of every color in card table
     */
    @Test
    void checkAllColorsAvailable() throws EmptyDeckException {
        CardTable cardTable = new CardTable(0);
        assertTrue(cardTable.checkAllColorsAvailable());

        for (int i = 0; i < 11; i++)
            cardTable.discardTop(CardColor.GREEN);
        assertTrue(cardTable.checkAllColorsAvailable());

        cardTable.discardTop(CardColor.GREEN);
        assertFalse(cardTable.checkAllColorsAvailable());
    }
}