package model.storage;

import Exceptions.*;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaderDepotTest {

    /**
     * This method tests the addition of resources to the depot (up to the maximum amount)
     */
    @Test
    void addResource() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD);
        ResourceDepot stashBlock = new BasicDepot(warehouse, 4);
        ResourceDepot stashLeader = new LeaderDepot(2, ResourceType.SHIELD);
        warehouse.addNewDepot(stash);
        warehouse.addNewDepot(stashBlock);
        warehouse.addNewDepot(stashLeader);

        stashBlock.addResource(ResourceType.SHIELD, 1);
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
        ResourceDepot stash = new LeaderDepot(4, ResourceType.SHIELD);

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
    void addResourceNotEnoughSpaceOldResource() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD);

        stash.addResource(ResourceType.SHIELD, 1);

        Exception ex = assertThrows(NotEnoughSpaceException.class, () -> {
            stash.addResource(ResourceType.SHIELD, 5);
        });

        String expectedMessage = "Error: There is no space left in storage for this type of resource.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the addition of a resource to a depot that can only store a different kind of resource
     */
    @Test
    void addResourceWrongResourceType() {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD);

        Exception ex = assertThrows(WrongResourceTypeException.class, () -> {
            stash.addResource(ResourceType.COIN, 1);
        });

        String expectedMessage = "Error: This type of resource cannot be inserted into this storage.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the canHoldContentOf method in the affirmative case
     */
    @Test
    void canHoldContentOfTrue() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 3);
        ResourceDepot stash2 = new BasicDepot(warehouse, 3);
        ResourceDepot stashLeader1 = new LeaderDepot(5, ResourceType.SHIELD);
        ResourceDepot stashLeader2 = new LeaderDepot(5, ResourceType.SHIELD);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);
        warehouse.addNewDepot(stashLeader1);
        warehouse.addNewDepot(stashLeader2);

        stash2.addResource(ResourceType.SHIELD, 2);
        stash1.addResource(ResourceType.COIN, 3);
        stashLeader1.addResource(ResourceType.SHIELD, 3);
        stashLeader2.addResource(ResourceType.SHIELD, 5);

        assertTrue(stashLeader1.canHoldContentOf(stash2));
        assertTrue(stashLeader1.canHoldContentOf(stashLeader2));
    }

    /**
     * This method tests the canHoldContentOf method in the affirmative case when one or both depots are empty
     */
    @Test
    void canHoldContentOfTrueEmpty() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash = new BasicDepot(warehouse, 3);
        ResourceDepot stashLeader1 = new LeaderDepot(5, ResourceType.SHIELD);
        ResourceDepot stashLeader2 = new LeaderDepot(5, ResourceType.SHIELD);
        warehouse.addNewDepot(stash);
        warehouse.addNewDepot(stashLeader1);
        warehouse.addNewDepot(stashLeader2);

        assertTrue(stashLeader1.canHoldContentOf(stash));
        assertTrue(stashLeader1.canHoldContentOf(stashLeader2));

        stashLeader1.addResource(ResourceType.SHIELD, 2);

        assertTrue(stashLeader1.canHoldContentOf(stash));
        assertTrue(stashLeader1.canHoldContentOf(stashLeader2));

        stashLeader1.clear();

        stashLeader2.addResource(ResourceType.SHIELD, 4);
        stash.addResource(ResourceType.SHIELD, 2);

        assertTrue(stashLeader1.canHoldContentOf(stash));
        assertTrue(stashLeader1.canHoldContentOf(stashLeader2));
    }


    /**
     * This method tests the canHoldContentOf method in the negative case for size constraints
     */
    @Test
    void canHoldContentOfFalseSize() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 5);
        ResourceDepot stashLeader1 = new LeaderDepot(2, ResourceType.SHIELD);
        ResourceDepot stashLeader2 = new LeaderDepot(5, ResourceType.SHIELD);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stashLeader1);
        warehouse.addNewDepot(stashLeader2);

        stash1.addResource(ResourceType.SHIELD, 5);
        stashLeader2.addResource(ResourceType.SHIELD, 3);

        assertFalse(stashLeader1.canHoldContentOf(stash1));
        assertFalse(stashLeader1.canHoldContentOf(stashLeader2));
    }

    /**
     * This method tests the canHoldContentOf method in the negative case for resource type constraints
     */
    @Test
    void canHoldContentOfFalseResourceType() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 5);
        ResourceDepot stashLeader1 = new LeaderDepot(5, ResourceType.SHIELD);
        ResourceDepot stashLeader2 = new LeaderDepot(5, ResourceType.COIN);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stashLeader1);
        warehouse.addNewDepot(stashLeader2);

        stash1.addResource(ResourceType.COIN, 3);
        stashLeader2.addResource(ResourceType.COIN, 5);

        assertFalse(stashLeader1.canHoldContentOf(stash1));
        assertFalse(stashLeader1.canHoldContentOf(stashLeader2));
    }

    /**
     * This method test the storage's blocking mechanic for resources
     */
    @Test
    void isBlocking() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);
        assertFalse(stash.isBlocking(ResourceType.SHIELD));
    }

    /**
     * This method tests the storage's maximum size
     */
    @Test
    void getSize() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);
        assertEquals(2, stash.getSize());
    }

    /**
     * This method tests the emptying of the storage's contents
     */
    @Test
    void empty() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);

        stash.addResource(ResourceType.SHIELD, 2);

        stash.clear();
        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of resources from the storage
     */
    @Test
    void removeResource() throws NotEnoughResourceException, BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD);

        stash.addResource(ResourceType.SHIELD, 4);
        stash.removeResource(ResourceType.SHIELD, 1);
        stash.removeResource(ResourceType.SHIELD, 2);

        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of an amount of resource greater than that present in stash
     */
    @Test
    void removeResourceNotEnough() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD);

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
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD);

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });

        String expectedMessage = "Error: Resource is not present in sufficient quantity.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the removal of a resource that cannot be present in stash
     */
    @Test
    void removeResourceNotInStash() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.COIN);

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
    void getNumOfResource() throws NotEnoughResourceException, BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(25, ResourceType.SHIELD);

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
    void getNumOfResourceRemoved() throws NotEnoughResourceException, BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(25, ResourceType.SHIELD);

        stash.addResource(ResourceType.SHIELD, 17);
        stash.removeResource(ResourceType.SHIELD, 17);


        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method test the reaction of the getStoredResources method to resources being added and removed from stash
     */
    @Test
    void getStoredResources() throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD);

        assertTrue(stash.getStoredResources().isEmpty());

        stash.addResource(ResourceType.SHIELD, 4);

        List<ResourceType> output = stash.getStoredResources();
        assertTrue(output.contains(ResourceType.SHIELD));
        assertFalse(output.contains(ResourceType.COIN));
    }
}