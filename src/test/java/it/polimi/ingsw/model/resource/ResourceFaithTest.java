package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.resource.ResourceFaith;
import it.polimi.ingsw.model.resource.ResourceServant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceFaithTest {

    /**
     * Tests the equals method for a specific resource
     */
    @Test
    void equals() {
        assertTrue(new ResourceFaith().equals(new ResourceFaith()));
        assertFalse(new ResourceFaith().equals(new ResourceServant()));
    }

    /**
     * Tests the addition of the resource to a player board when taken from the market
     */
    @Test
    void addResourceFromMarket() {
        PlayerBoard playerBoard = new PlayerBoard();
        ResourceFaith faith = new ResourceFaith();

        faith.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getFaith());

        faith.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getFaith());
    }

    /**
     * Tests the addition of the resource to a player board when obtained from a production
     */
    @Test
    void addResourceFromProduction() {
        PlayerBoard playerBoard = new PlayerBoard();
        ResourceFaith faith = new ResourceFaith();

        faith.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getFaith());

        faith.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getFaith());
    }

}