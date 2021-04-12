package model.resource;

import Exceptions.*;
import model.PlayerBoard;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceShieldTest {

    @Test
    void equals() {
        ResourceShield shield = new ResourceShield();
        ResourceShield shield1 = new ResourceShield();

        assertTrue(shield.equals(shield1));
    }

    @Test
    void removeList() {
        ArrayList<Resource> list = new ArrayList<>();
        list.add(new ResourceShield());
        list.add(new ResourceFaith());
        list.remove(new ResourceShield());

        assertTrue(list.get(0) instanceof ResourceFaith);
    }

    @Test
    public void addResourceFromMarket() throws DepotNotPresentException, WrongResourceTypeException, NotEnoughSpaceException, BlockedResourceException, NotEnoughResourceException {
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, null);
        ResourceShield shield = new ResourceShield();

        shield.addResourceFromMarket(playerBoard);

        playerBoard.sendResourceToDepot(1, ResourceType.SHIELD, 1);
        assertEquals(1, playerBoard.getNumOfResource(ResourceType.SHIELD));

    }

}
