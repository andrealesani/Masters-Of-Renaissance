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
        PlayerBoard playerBoard = new PlayerBoard();

        cardTable.buyTopCard(CardColor.GREEN, 3, playerBoard, 1);
        
    }
}