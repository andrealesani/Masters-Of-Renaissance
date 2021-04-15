package model.storage;

import Exceptions.*;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicDepotTest {

    /**
     * This method tests the addition of resources to the depot (up to the maximum amount)
     */
    @Test
    void addResource() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash = new BasicDepot(warehouse, 4);
        ResourceDepot stashBlock = new BasicDepot(warehouse, 4);
        ResourceDepot stashLeader = new LeaderDepot(2, ResourceType.SHIELD);
        warehouse.addNewDepot(stash);
        warehouse.addNewDepot(stashBlock);
        warehouse.addNewDepot(stashLeader);

        stashBlock.addResource(ResourceType.COIN, 1);
        stashLeader.addResource(ResourceType.SHIELD, 1);
        stash.addResource(ResourceType.SHIELD, 1);

        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));

        stash.addResource(ResourceType.SHIELD, 2);
        stash.addResource(ResourceType.SHIELD, 1);

        assertEquals(4, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the addition of a new resource in amount greater than the size of the depot
     */
    @Test
    void addResourceNotEnoughSpaceNewResource() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 4);

        Exception ex = assertThrows(NotEnoughSpaceException.class, () -> {
            stash.addResource(ResourceType.SHIELD, 5);
        });

        String expectedMessage = "Error: There is no space left in storage for this type of resource.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the addition of a resource already present in the depot, for a total amount greater than the size of the depot
     */
    @Test
    void addResourceNotEnoughSpaceOldResource() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 4);


        stash.addResource(ResourceType.SHIELD, 1);


        Exception ex = assertThrows(NotEnoughSpaceException.class, () -> {
            stash.addResource(ResourceType.SHIELD, 5);
        });

        String expectedMessage = "Error: There is no space left in storage for this type of resource.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the addition of a resource to a depot already storing one of different type of resource
     */
    @Test
    void addResourceWrongResourceType() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 4);

        stash.addResource(ResourceType.SHIELD, 1);

        Exception ex = assertThrows(WrongResourceInsertionException.class, () -> {
            stash.addResource(ResourceType.COIN, 1);
        });

        String expectedMessage = "Error: This type of resource cannot be inserted into this storage.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the addition of a resource to an empty basic depot, when a different basic depot is already storing that same type of resource
     */
    @Test
    void addResourceBlockedResourceType() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 1);
        ResourceDepot stash2 = new BasicDepot(warehouse, 2);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);

        stash1.addResource(ResourceType.SHIELD, 1);

        Exception ex = assertThrows(BlockedResourceException.class, () -> {
            stash2.addResource(ResourceType.SHIELD, 2);
        });
    }

    /**
     * This method tests the canHoldContentOf method in the affirmative case
     */
    @Test
    void canHoldContentOfTrue() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 3);
        ResourceDepot stash2 = new BasicDepot(warehouse, 3);
        ResourceDepot stashLeader = new LeaderDepot(5, ResourceType.COIN);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);
        warehouse.addNewDepot(stashLeader);

        stash2.addResource(ResourceType.SHIELD, 2);
        stashLeader.addResource(ResourceType.COIN, 2);
        stash1.addResource(ResourceType.COIN, 3);

        assertTrue(stash1.canHoldContentOf(stash2));
        assertTrue(stash1.canHoldContentOf(stashLeader));
    }

    /**
     * This method tests the canHoldContentOf method in the affirmative case when one or both depots are empty
     */
    @Test
    void canHoldContentOfTrueEmpty() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 3);
        ResourceDepot stash2 = new BasicDepot(warehouse, 3);
        ResourceDepot stashLeader = new LeaderDepot(5, ResourceType.COIN);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);
        warehouse.addNewDepot(stashLeader);

        assertTrue(stash1.canHoldContentOf(stash2));
        assertTrue(stash1.canHoldContentOf(stashLeader));

        stash1.addResource(ResourceType.COIN, 3);

        assertTrue(stash1.canHoldContentOf(stash2));
        assertTrue(stash1.canHoldContentOf(stashLeader));

        stash1.clear();

        stash2.addResource(ResourceType.SHIELD, 2);
        stashLeader.addResource(ResourceType.COIN, 2);

        assertTrue(stash1.canHoldContentOf(stash2));
        assertTrue(stash1.canHoldContentOf(stashLeader));
    }

    /**
     * This method tests the canHoldContentOf method in the negative case for size constraints
     */
    @Test
    void canHoldContentOfFalseSize() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 1);
        ResourceDepot stash2 = new BasicDepot(warehouse, 2);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);

        stash2.addResource(ResourceType.SHIELD, 2);

        assertFalse(stash1.canHoldContentOf(stash2));
    }

    /**
     * This method tests the canHoldContentOf method in the negative case for blocking constraints
     */
    @Test
    void canHoldContentOfFalseBlocking() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 3);
        ResourceDepot stash2 = new LeaderDepot(3, ResourceType.SHIELD);
        ResourceDepot stash3 = new BasicDepot(warehouse, 3);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);
        warehouse.addNewDepot(stash3);

        stash3.addResource(ResourceType.SHIELD, 2);
        stash2.addResource(ResourceType.SHIELD, 2);

        assertFalse(stash1.canHoldContentOf(stash2));
    }

    /**
     * This method test the storage's blocking mechanic for resources in the affirmative case
     */
    @Test
    void isBlockingTrue() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 3);

        stash.addResource(ResourceType.SHIELD, 1);

        assertTrue(stash.isBlocking(ResourceType.SHIELD));
    }

    /**
     * This method test the storage's blocking mechanic for resources in the negative case
     */
    @Test
    void isBlockingFalse() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 3);

        stash.addResource(ResourceType.SHIELD, 1);

        assertFalse(stash.isBlocking(ResourceType.COIN));
    }

    /**
     * This method tests the storage's maximum size
     */
    @Test
    void getSize() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 3);
        assertEquals(3, stash.getSize());
    }

    /**
     * This method tests the emptying of the storage's contents
     */
    @Test
    void empty() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 3);

        stash.addResource(ResourceType.SHIELD, 2);

        stash.clear();
        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of resources from the storage
     */
    @Test
    void removeResource() throws NotEnoughResourceException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 3);

        stash.addResource(ResourceType.SHIELD, 2);
        stash.removeResource(ResourceType.SHIELD, 1);

        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of an amount of resource greater than that present in stash
     */
    @Test
    void removeResourceNotEnough() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 3);

        stash.addResource(ResourceType.SHIELD, 3);

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });

        String expectedMessage = "Error: Resource is not present in sufficient quantity.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the removal of a resource from an empty depot
     */
    @Test
    void removeResourceEmptyDepot() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 3);

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });

        String expectedMessage = "Error: Resource is not present in sufficient quantity.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the removal of a resource that is not present in the stash
     */
    @Test
    void removeResourceNotInStash() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 3);

        stash.addResource(ResourceType.COIN, 3);

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });

        String expectedMessage = "Error: Resource is not present in sufficient quantity.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the reaction of the getNumOfResource method to several operations being made on the fetched resource
     */
    @Test
    void getNumOfResource() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, NotEnoughResourceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 25);

        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
        assertEquals(0, stash.getNumOfResource(ResourceType.COIN));

        stash.addResource(ResourceType.SHIELD, 17);
        stash.removeResource(ResourceType.SHIELD, 16);
        stash.addResource(ResourceType.SHIELD, 22);

        assertEquals(23, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the reaction of the getNumOfResource method to a previously present resource being deleted from the stash
     */
    @Test
    void getNumOfResourceRemoved() throws NotEnoughResourceException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 25);

        stash.addResource(ResourceType.SHIELD, 17);
        stash.removeResource(ResourceType.SHIELD, 17);

        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method test the reaction of the getStoredResources method to resources being added and removed from stash
     */
    @Test
    void getStoredResources() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, NotEnoughResourceException {
        ResourceDepot stash = new BasicDepot(new Warehouse(0), 5);

        assertTrue(stash.getStoredResources().isEmpty());

        stash.addResource(ResourceType.SHIELD, 4);

        List<ResourceType> output = stash.getStoredResources();
        assertTrue(output.contains(ResourceType.SHIELD));
        assertFalse(output.contains(ResourceType.COIN));

        stash.removeResource(ResourceType.SHIELD, 4);

        output = stash.getStoredResources();
        assertFalse(output.contains(ResourceType.SHIELD));
    }
}