package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.NotEnoughSpaceException;
import Exceptions.ResourceNotPresentException;
import Exceptions.WrongResourceTypeException;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderDepotTest {

    /**
     * This method tests the addition of resources to the storage
     */
    @Test
    void addResource() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);
        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the holding capacity of the storage
     */
    @Test
    void canHold() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);
        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        assertTrue(stash.canHold(ResourceType.SHIELD, 2));
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
    void empty() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);
        try {
            stash.addResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughSpaceException | WrongResourceTypeException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        stash.empty();
        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of resources from the storage
     */
    @Test
    void removeResource() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);
        try {
            stash.addResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughSpaceException | WrongResourceTypeException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        try {
            stash.removeResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughResourceException | ResourceNotPresentException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the reaction of the getNumOfResource method to an empty storage and the correct working of the constructor
     */
    @Test
    void getNumOfResourceEmpty() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);
        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the reaction of the getStoredResources method to an empty storage and the correct working of the constructor
     */
    @Test
    void getStoredResourcesEmpty() {
        ResourceDepot stash = new LeaderDepot(2, ResourceType.SHIELD);
        assertTrue(stash.getStoredResources().isEmpty());
    }
}