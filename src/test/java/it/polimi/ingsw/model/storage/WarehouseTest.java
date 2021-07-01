package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    /**
     * This method tests the addition and getting of the warehouse's depots
     */
    @Test
    void addAndGetNumOfDepots() {
        Warehouse warehouse = new Warehouse (3);

        assertEquals (3, warehouse.getNumOfDepots());

        ResourceDepot stashLeader1 = new LeaderDepot(3, ResourceType.SHIELD, 0);
        ResourceDepot stashLeader2 = new LeaderDepot(3, ResourceType.COIN, 0);
        warehouse.addNewDepot(stashLeader1);
        warehouse.addNewDepot(stashLeader2);

        assertEquals (5, warehouse.getNumOfDepots());

        assertSame (stashLeader1, warehouse.getDepot(4));
        assertSame (stashLeader2, warehouse.getDepot(5));
    }

    /**
     * This method tests the addition of resources to depots
     */
    @Test
    void addToDepot() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException {
        Warehouse warehouse = new Warehouse (2);
        ResourceDepot stash1 = warehouse.getDepot(1);
        ResourceDepot stash2 = warehouse.getDepot(2);
        ResourceDepot stashLeader = new LeaderDepot(3, ResourceType.SHIELD, 0);
        warehouse.addNewDepot(stashLeader);

        warehouse.addToDepot (1, ResourceType.SHIELD, 1);
        warehouse.addToDepot (2, ResourceType.COIN, 1);
        warehouse.addToDepot (2, ResourceType.COIN, 1);
        warehouse.addToDepot (3, ResourceType.SHIELD, 3);

        assertEquals (1, stash1.getNumOfResource(ResourceType.SHIELD));
        assertEquals (2, stash2.getNumOfResource(ResourceType.COIN));
        assertEquals (3, stashLeader.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the addition of resources to depots that do not exist
     */
    @Test
    void addToDepotDepotNotPresent() {
        Warehouse warehouse = new Warehouse (2);
        warehouse.addNewDepot(new LeaderDepot(3, ResourceType.SHIELD, 0));

        Exception ex = assertThrows(ParametersNotValidException.class, () -> {
            warehouse.addToDepot(0, ResourceType.SHIELD, 1);
        });

        ex = assertThrows(DepotNotPresentException.class, () -> {
            warehouse.addToDepot(4, ResourceType.SHIELD, 1);
        });
    }

    /**
     * This method test the removal of resources from depots
     */
    @Test
    void removeFromDepot() throws NotEnoughResourceException, DepotNotPresentException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse (2);
        ResourceDepot stash1 = warehouse.getDepot(1);
        ResourceDepot stash2 = warehouse.getDepot(2);
        ResourceDepot stashLeader = new LeaderDepot(3, ResourceType.SHIELD, 0);
        warehouse.addNewDepot(stashLeader);


        warehouse.addToDepot (1, ResourceType.SHIELD, 1);
        warehouse.addToDepot (2, ResourceType.COIN, 2);
        warehouse.addToDepot (3, ResourceType.SHIELD, 3);

        warehouse.removeFromDepot(1, ResourceType.SHIELD, 1);
        warehouse.removeFromDepot(2, ResourceType.COIN, 1);
        warehouse.removeFromDepot(3, ResourceType.SHIELD, 1);
        warehouse.removeFromDepot(3, ResourceType.SHIELD, 1);


        assertEquals (0, stash1.getNumOfResource(ResourceType.SHIELD));
        assertEquals (1, stash2.getNumOfResource(ResourceType.COIN));
        assertEquals (1, stashLeader.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of resources from depots that do not exist
     */
    @Test
    void removeFromDepotDepotNotPresent() {
        Warehouse warehouse = new Warehouse(3);
        warehouse.addNewDepot(new LeaderDepot(3, ResourceType.SHIELD, 0));
        warehouse.addNewDepot(new LeaderDepot(3, ResourceType.SHIELD, 0));

        Exception ex = assertThrows(ParametersNotValidException.class, () -> {
            warehouse.removeFromDepot(0, ResourceType.SHIELD, 1);
        });

        ex = assertThrows(DepotNotPresentException.class, () -> {
            warehouse.removeFromDepot(6, ResourceType.SHIELD, 1);
        });

    }

    /**
     * This method tests the swapping of two empty depots
     */
    @Test
    void swapDepotContentBothEmpty() throws SwapNotValidException, DepotNotPresentException, ParametersNotValidException {
        Warehouse warehouse = new Warehouse (1);
        ResourceDepot stash = warehouse.getDepot(1);
        ResourceDepot stashLeader = new LeaderDepot(3, ResourceType.SHIELD, 0);
        warehouse.addNewDepot(stashLeader);



        warehouse.swapDepotContent(1,2);


        assertTrue(stash.getStoredResources().isEmpty());
        assertTrue(stashLeader.getStoredResources().isEmpty());
    }

    /**
     * This method tests the swapping of two depots, one of which is empty
     */
    @Test
    void swapDepotContentOneEmpty() throws SwapNotValidException, DepotNotPresentException, ParametersNotValidException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse (1);
        ResourceDepot stash = warehouse.getDepot(1);
        ResourceDepot stashLeader = new LeaderDepot(3, ResourceType.SHIELD, 0);
        warehouse.addNewDepot(stashLeader);

        warehouse.addToDepot (1, ResourceType.SHIELD, 1);

        warehouse.swapDepotContent(1,2);


        assertTrue(stash.getStoredResources().isEmpty());
        assertEquals(1, stashLeader.getNumOfResource(ResourceType.SHIELD));


        warehouse.swapDepotContent(1,2);


        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
        assertTrue(stashLeader.getStoredResources().isEmpty());
    }

    /**
     * This method tests the swapping of two depots, both filled
     */
    @Test
    void swapDepotContentBothFilled() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, SwapNotValidException, ParametersNotValidException {
        Warehouse warehouse = new Warehouse (2);
        ResourceDepot stash1 = warehouse.getDepot(1);
        ResourceDepot stash2 = warehouse.getDepot(2);
        ResourceDepot stashLeader = new LeaderDepot(3, ResourceType.SHIELD, 0);
        warehouse.addNewDepot(stashLeader);


        warehouse.addToDepot (1, ResourceType.SHIELD, 1);
        warehouse.addToDepot (2, ResourceType.COIN, 1);
        warehouse.addToDepot (3, ResourceType.SHIELD, 2);

        warehouse.swapDepotContent(1,2);


        assertEquals(1, stash1.getNumOfResource(ResourceType.COIN));
        assertEquals(1, stash2.getNumOfResource(ResourceType.SHIELD));


        warehouse.swapDepotContent(2,3);


        assertEquals(2, stash2.getNumOfResource(ResourceType.SHIELD));
        assertEquals(1, stashLeader.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the swapping of a depot with itself
     */
    @Test
    void swapDepotParametersNotValid() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException {
        Warehouse warehouse = new Warehouse (1);


        warehouse.addToDepot (1, ResourceType.SHIELD, 1);


        Exception ex = assertThrows(ParametersNotValidException.class, () -> {
            warehouse.swapDepotContent(1,1);
        });
    }

    /**
     * This method tests the swapping of two depots where one or both do not exist
     */
    @Test
    void swapDepotDepotNotPresent() {
        Warehouse warehouse = new Warehouse (1);

        Exception ex = assertThrows(ParametersNotValidException.class, () -> {
            warehouse.swapDepotContent(1,0);
        });

        ex = assertThrows(DepotNotPresentException.class, () -> {
            warehouse.swapDepotContent(2,1);
        });
    }

    /**
     * This method tests the swapping of two depots where one or both cannot hold the resources of the other
     */
    @Test
    void swapDepotSwapNotValid() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException {
        Warehouse warehouse = new Warehouse (3);
        warehouse.addNewDepot(new LeaderDepot(5, ResourceType.COIN, 0));
        warehouse.addNewDepot(new LeaderDepot(5, ResourceType.SERVANT, 0));



        warehouse.addToDepot (1, ResourceType.STONE, 0);
        warehouse.addToDepot (2, ResourceType.COIN, 2);
        warehouse.addToDepot (3, ResourceType.STONE, 3);
        warehouse.addToDepot (4, ResourceType.COIN, 1);
        warehouse.addToDepot (5, ResourceType.SERVANT, 1);


        //Testing for quantity constraints
        Exception ex = assertThrows(SwapNotValidException.class, () -> {
            warehouse.swapDepotContent(2,3);
        });

        //Testing for blocking constraints
        ex = assertThrows(SwapNotValidException.class, () -> {
                    warehouse.swapDepotContent(1, 4);
        });

        //Testing for resource type constraints
        ex = assertThrows(SwapNotValidException.class, () -> {
            warehouse.swapDepotContent(3,5);
        });
    }

    /**
     * This method tests the movement of a resource from a depot to another
     */
    @Test
    void moveDepotContent () throws WrongResourceInsertionException, BlockedResourceException, NotEnoughSpaceException, DepotNotPresentException, NotEnoughResourceException {
        Warehouse warehouse = new Warehouse (2);
        ResourceDepot stash1 = warehouse.getDepot(1);
        ResourceDepot stash2 = warehouse.getDepot(2);
        ResourceDepot stashLeader = new LeaderDepot(3, ResourceType.SHIELD, 0);
        warehouse.addNewDepot(stashLeader);

        warehouse.addToDepot (1, ResourceType.COIN, 1);
        warehouse.addToDepot (2, ResourceType.SHIELD, 1);
        warehouse.addToDepot (3, ResourceType.SHIELD, 1);

        warehouse.moveDepotContent(2,3, ResourceType.SHIELD,1);

        assertEquals(0, warehouse.getDepot(2).getNumOfResource(ResourceType.SHIELD));
        assertEquals(2, warehouse.getDepot(3).getNumOfResource(ResourceType.SHIELD));

        warehouse.moveDepotContent(3,2, ResourceType.SHIELD,2);

        assertEquals(2, warehouse.getDepot(2).getNumOfResource(ResourceType.SHIELD));
        assertEquals(0, warehouse.getDepot(3).getNumOfResource(ResourceType.SHIELD));

    }

    /**
     * This method tests the blocking of a resource for the affirmative case
     */
    @Test
    void isResourceBlockedTrue() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException {
        Warehouse warehouse = new Warehouse (2);

        warehouse.addToDepot (1, ResourceType.SHIELD, 1);
        warehouse.addToDepot (2, ResourceType.COIN, 2);


        assertTrue(warehouse.isResourceBlocked(ResourceType.SHIELD, null));
        assertTrue(warehouse.isResourceBlocked(ResourceType.COIN, null));
    }

    /**
     * This method tests the blocking of a resource for the negative case
     */
    @Test
    void isResourceFalse() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException {
        Warehouse warehouse = new Warehouse (2);
        ResourceDepot stash2 = warehouse.getDepot(2);
        warehouse.addNewDepot(new LeaderDepot(5, ResourceType.SHIELD, 0));


        warehouse.addToDepot (2, ResourceType.COIN, 2);
        warehouse.addToDepot (3, ResourceType.SHIELD, 1);


        List<ResourceDepot> exclusions = new ArrayList<>();
        exclusions.add(stash2);

        assertFalse(warehouse.isResourceBlocked(ResourceType.SHIELD, exclusions));
        assertFalse(warehouse.isResourceBlocked(ResourceType.COIN, exclusions));
    }

    /**
     * This method tests the getNumOfResourceMethod
     */
    @Test
    void getNumOfResource() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, NotEnoughResourceException {
        Warehouse warehouse = new Warehouse (3);
        ResourceDepot stashLeader1 = new LeaderDepot(5, ResourceType.SHIELD, 0);
        ResourceDepot stashLeader2 = new LeaderDepot(5, ResourceType.COIN, 0);
        warehouse.addNewDepot(stashLeader1);
        warehouse.addNewDepot(stashLeader2);


        warehouse.addToDepot (1, ResourceType.COIN, 1);
        warehouse.addToDepot (2, ResourceType.STONE, 1);
        warehouse.addToDepot (3, ResourceType.SERVANT, 2);
        warehouse.addToDepot (4, ResourceType.SHIELD, 3);
        warehouse.addToDepot (5, ResourceType.COIN, 4);

        warehouse.removeFromDepot(2, ResourceType.STONE, 1);

        assertEquals(0, warehouse.getNumOfResource(ResourceType.STONE));
        assertEquals(3, warehouse.getNumOfResource(ResourceType.SHIELD));
        assertEquals(2, warehouse.getNumOfResource(ResourceType.SERVANT));
        assertEquals(5, warehouse.getNumOfResource(ResourceType.COIN));

        stashLeader1.clear();
        stashLeader2.clear();

        assertEquals(0, warehouse.getNumOfResource(ResourceType.SHIELD));
        assertEquals(1, warehouse.getNumOfResource(ResourceType.COIN));
    }

    /**
     * This method tests the getStoredResources method
     */
    @Test
    void getStoredResources() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, NotEnoughResourceException {
        Warehouse warehouse = new Warehouse (3);
        ResourceDepot stashLeader1 = new LeaderDepot(5, ResourceType.SHIELD, 0);
        ResourceDepot stashLeader2 = new LeaderDepot(5, ResourceType.COIN, 0);
        warehouse.addNewDepot(stashLeader1);
        warehouse.addNewDepot(stashLeader2);

        assertTrue (warehouse.getStoredResources().isEmpty());


        warehouse.addToDepot (1, ResourceType.COIN, 1);
        warehouse.addToDepot (2, ResourceType.STONE, 1);
        warehouse.addToDepot (3, ResourceType.SERVANT, 2);
        warehouse.addToDepot (4, ResourceType.SHIELD, 3);
        warehouse.addToDepot (5, ResourceType.COIN, 4);

        stashLeader1.clear();

        warehouse.removeFromDepot(2, ResourceType.STONE, 1);

        List<ResourceType> resourceList = warehouse.getStoredResources();

        assertEquals(2, resourceList.size());
        assertTrue(resourceList.contains(ResourceType.SERVANT));
        assertTrue(resourceList.contains(ResourceType.COIN));
        assertFalse(resourceList.contains(ResourceType.SHIELD));
        assertFalse(resourceList.contains(ResourceType.STONE));
    }
}