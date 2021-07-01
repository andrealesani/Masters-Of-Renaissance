package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.resource.ResourceCoin;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.resource.ResourceWhite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceWhiteTest {
    /**
     * Tests the equals method for a specific resource
     */
    @Test
    void equals() {
        assertTrue(new ResourceWhite().equals(new ResourceWhite()));
        assertFalse(new ResourceWhite().equals(new ResourceCoin()));
    }

    /**
     * Tests the addition of the resource to a player board when taken from the market and having no white marble conversions
     */
    @Test
    void addResourceFromMarketNoConversion() {
        PlayerBoard playerBoard = new PlayerBoard();
        ResourceWhite white = new ResourceWhite();

        white.addResourceFromMarket(playerBoard);
        assertEquals(0, playerBoard.getLeftInWaitingRoom());

    }

    /**
     * Tests the addition of the resource to a player board when taken from the market and having one marble conversion
     */
    @Test
    void addResourceFromMarketWithConversion() throws DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException, NotEnoughResourceException {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addMarbleConversion(ResourceType.SHIELD);
        ResourceWhite white = new ResourceWhite();

        white.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getLeftInWaitingRoom());

        playerBoard.sendResourceToDepot(1, ResourceType.SHIELD, 1 );
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.SHIELD));

    }

    /**
     * Tests the addition of the resource to a player board when taken from the market and having multiple marble conversions
     */
    @Test
    void addResourceFromMarketMultipleConversion() throws DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException, NotEnoughResourceException, ConversionNotAvailableException {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addMarbleConversion(ResourceType.SHIELD);
        playerBoard.addMarbleConversion(ResourceType.COIN);
        ResourceWhite white = new ResourceWhite();

        white.addResourceFromMarket(playerBoard);
        white.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getLeftInWaitingRoom());

        playerBoard.chooseMarbleConversion(ResourceType.SHIELD, 1);
        assertEquals(2, playerBoard.getLeftInWaitingRoom());

        playerBoard.sendResourceToDepot(1, ResourceType.SHIELD, 1 );
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.SHIELD));

        playerBoard.chooseMarbleConversion(ResourceType.COIN, 1);
        assertEquals(1, playerBoard.getLeftInWaitingRoom());

        playerBoard.sendResourceToDepot(2, ResourceType.COIN, 1 );
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.COIN));

    }
}