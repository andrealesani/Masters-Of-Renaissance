package model.resource;

import Exceptions.*;
import model.PlayerBoard;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCoinTest {

    @Test
    void equals() {
        assertTrue(new ResourceCoin().equals(new ResourceCoin()));
        assertFalse(new ResourceCoin().equals(new ResourceServant()));
    }

    @Test
    void removeList(){
        ArrayList<Resource> list = new ArrayList<>();
        list.add(new ResourceCoin());
        list.add(new ResourceFaith());
        list.remove(new ResourceCoin());

        assertTrue(list.get(0) instanceof ResourceFaith);
    }

    @Test
    public void addResourceFromMarket() throws DepotNotPresentException, WrongResourceTypeException, NotEnoughSpaceException, BlockedResourceException, NotEnoughResourceException {
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null);
        ResourceCoin coin = new ResourceCoin();

        coin.addResourceFromMarket(playerBoard);

        playerBoard.sendResourceToDepot(1, ResourceType.COIN, 1 );
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.COIN));

    }

}