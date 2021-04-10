package model.storage;

import Exceptions.*;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicDepotTest {

    /**
     * This method tests the addition of resources to the storage
     */
    @Test
    void addResource() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /*
    @Test
    void canHold() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        assertTrue(stash.canHold(ResourceType.COIN, 2));
    }
    */


    /**
     * This method test the storage's blocking mechanic for resources in the affirmative case
     */
    @Test
    void isBlockingTrue() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        assertTrue(stash.isBlocking(ResourceType.SHIELD));
    }

    /**
     * This method test the storage's blocking mechanic for resources in the negative case
     */
    @Test
    void isBlockingFalse() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        assertFalse(stash.isBlocking(ResourceType.COIN));
    }

    /**
     * This method tests the storage's maximum size
     */
    @Test
    void getSize() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        assertEquals(3, stash.getSize());
    }

    /**
     * This method tests the emptying of the storage's contents
     */
    @Test
    void empty() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        try {
            stash.addResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        stash.clear();
        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of resources from the storage
     */
    @Test
    void removeResource() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        try {
            stash.addResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        try {
            stash.removeResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughResourceException ex) {
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
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the reaction of the getStoredResources method to an empty storage and the correct working of the constructor
     */
    @Test
    void getStoredResourcesEmpty() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);
        assertTrue(stash.getStoredResources().isEmpty());
    }
}