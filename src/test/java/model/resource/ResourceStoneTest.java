package model.resource;

import Exceptions.*;
import model.PlayerBoard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceStoneTest {

    @Test
    void equals() {
        assertTrue(new ResourceStone().equals(new ResourceStone()));
        assertFalse(new ResourceStone().equals(new ResourceUnknown()));
    }

    @Test
    void removeList() {
        ArrayList<Resource> list = new ArrayList<>();
        list.add(new ResourceStone());
        list.add(new ResourceFaith());
        list.remove(new ResourceStone());

        assertTrue(list.get(0) instanceof ResourceFaith);
    }

    @Test
    public void addResourceFromMarket() throws DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException, NotEnoughResourceException {
        PlayerBoard playerBoard = new PlayerBoard();
        ResourceStone stone = new ResourceStone();

        stone.addResourceFromMarket(playerBoard);

        playerBoard.sendResourceToDepot(1, ResourceType.STONE, 1);
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.STONE));

    }

}
