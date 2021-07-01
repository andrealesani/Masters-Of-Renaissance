package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.Exceptions.BlockedResourceException;
import it.polimi.ingsw.Exceptions.NotEnoughResourceException;
import it.polimi.ingsw.Exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.Exceptions.WrongResourceInsertionException;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaderDepotTest {

    /**
     * This method tests the addition of resources to the depot (up to the maximum amount)
     */
    @Test
    void addResource() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD, 0);
        ResourceDepot stashBlock = new BasicDepot(warehouse, 4);
        ResourceDepot stashLeader = new LeaderDepot(2, ResourceType.SHIELD, 0);
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
        ResourceDepot stash = new LeaderDepot(4, ResourceType.SHIELD, 0);

        Exception ex = assertThrows(NotEnoughSpaceException.class, () -> {
            stash.addResource(ResourceType.SHIELD, 5);
        });
    }

    /**
     * This method tests the addition of a resource already present in the depot, for a total amount greater than the size of the depot
     */
    @Test
    void addResourceNotEnoughSpaceOldResource() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD, 0);

        stash.addResource(ResourceType.SHIELD, 1);

        Exception ex = assertThrows(NotEnoughSpaceException.class, () -> {
            stash.addResource(ResourceType.SHIELD, 5);
        });
    }

    /**
     * This method tests the addition of a resource to a depot that can only store a different kind of resource
     */
    @Test
    void addResourceWrongResourceType() {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD, 0);

        Exception ex = assertThrows(WrongResourceInsertionException.class, () -> {
            stash.addResource(ResourceType.COIN, 1);
        });
    }

    /**
     * This method test the storage's blocking mechanic for resources
     */
    @Test
    void isBlocking() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD, 0);
        assertFalse(stash.isBlocking(ResourceType.SHIELD));
    }

    /**
     * This method tests the storage's maximum size
     */
    @Test
    void getSize() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD, 0);
        assertEquals(2, stash.getSize());
    }

    /**
     * This method tests the emptying of the storage's contents
     */
    @Test
    void empty() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD, 0);

        stash.addResource(ResourceType.SHIELD, 2);

        stash.clear();
        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of resources from the storage
     */
    @Test
    void removeResource() throws NotEnoughResourceException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD, 0);

        stash.addResource(ResourceType.SHIELD, 4);
        stash.removeResource(ResourceType.SHIELD, 1);
        stash.removeResource(ResourceType.SHIELD, 2);

        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of an amount of resource greater than that present in stash
     */
    @Test
    void removeResourceNotEnough() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD, 0);

        stash.addResource(ResourceType.SHIELD, 3);

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });
    }

    /**
     * This method tests the removal of a resource from an empty depot
     */
    @Test
    void removeResourceEmptyDepot() {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD, 0);

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });
    }

    /**
     * This method tests the removal of a resource that cannot be present in stash
     */
    @Test
    void removeResourceNotInStash() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.COIN, 0);

        stash.addResource(ResourceType.COIN, 3);

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });
    }

    /**
     * This method tests the reaction of the getNumOfResource method to several operations being made on the fetched resource
     */
    @Test
    void getNumOfResource() throws NotEnoughResourceException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(25, ResourceType.SHIELD, 0);

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
        ResourceDepot stash = new LeaderDepot(25, ResourceType.SHIELD, 0);

        stash.addResource(ResourceType.SHIELD, 17);
        stash.removeResource(ResourceType.SHIELD, 17);


        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method test the reaction of the getStoredResources method to resources being added and removed from stash
     */
    @Test
    void getStoredResources() throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        ResourceDepot stash = new LeaderDepot(5, ResourceType.SHIELD, 0);

        assertTrue(stash.getStoredResources().isEmpty());

        stash.addResource(ResourceType.SHIELD, 4);

        List<ResourceType> output = stash.getStoredResources();
        assertTrue(output.contains(ResourceType.SHIELD));
        assertFalse(output.contains(ResourceType.COIN));
    }
}