package model.resource;

import Exceptions.*;
import model.PlayerBoard;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceServantTest {

    @Test
    void equals() {
        ResourceServant servant = new ResourceServant();
        ResourceServant servant1 = new ResourceServant();

        assertTrue(servant.equals(servant1));
    }

    @Test
    void removeList() {
        ArrayList<Resource> list = new ArrayList<>();
        list.add(new ResourceServant());
        list.add(new ResourceFaith());
        list.remove(new ResourceServant());

        assertTrue(list.get(0) instanceof ResourceFaith);
    }

    @Test
    public void addResourceFromMarket() throws DepotNotPresentException, WrongResourceTypeException, NotEnoughSpaceException, BlockedResourceException, NotEnoughResourceException {
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, null);
        ResourceServant servant = new ResourceServant();

        servant.addResourceFromMarket(playerBoard);

        playerBoard.sendResourceToDepot(1, ResourceType.SERVANT, 1);
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.SERVANT));

    }

}
