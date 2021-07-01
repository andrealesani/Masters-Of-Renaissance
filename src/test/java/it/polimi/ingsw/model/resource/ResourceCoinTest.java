package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.model.PlayerBoard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCoinTest {

    /**
     * Tests the equals method for a specific resource
     */
    @Test
    void equals() {
        assertTrue(new ResourceCoin().equals(new ResourceCoin()));
        assertFalse(new ResourceCoin().equals(new ResourceServant()));
    }

    /**
     * Tests the removal of the resource from a list using the equals method
     */
    @Test
    void removeList(){
        ArrayList<Resource> list = new ArrayList<>();
        list.add(new ResourceCoin());
        list.add(new ResourceFaith());
        list.remove(new ResourceCoin());

        assertTrue(list.get(0) instanceof ResourceFaith);
    }

    /**
     * Tests the addition of the resource to a player board when taken from the market
     */
    @Test
    public void addResourceFromMarket() throws DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException, NotEnoughResourceException {
        PlayerBoard playerBoard = new PlayerBoard();
        ResourceCoin coin = new ResourceCoin();

        coin.addResourceFromMarket(playerBoard);

        playerBoard.sendResourceToDepot(1, ResourceType.COIN, 1 );
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.COIN));

    }
}