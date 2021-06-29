package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.resource.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceServantTest {

    @Test
    void equals() {
        assertTrue(new ResourceServant().equals(new ResourceServant()));
        assertFalse(new ResourceServant().equals(new ResourceJolly()));
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
    public void addResourceFromMarket() throws DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException, NotEnoughResourceException {
        PlayerBoard playerBoard = new PlayerBoard();
        ResourceServant servant = new ResourceServant();

        servant.addResourceFromMarket(playerBoard);

        playerBoard.sendResourceToDepot(1, ResourceType.SERVANT, 1);
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.SERVANT));

    }

}
