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
    void addResource() {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash = new BasicDepot(warehouse, 4);
        ResourceDepot stashBlock = new BasicDepot(warehouse, 4);
        ResourceDepot stashLeader = new LeaderDepot(2, ResourceType.SHIELD);
        warehouse.addNewDepot(stash);
        warehouse.addNewDepot(stashBlock);
        warehouse.addNewDepot(stashLeader);

        try {
            stashBlock.addResource(ResourceType.COIN, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        try {
            stashLeader.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));

        try {
            stash.addResource(ResourceType.SHIELD, 2);
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertEquals(4, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the addition of a new resource in amount greater than the size of the depot
     */
    @Test
    void addResourceNotEnoughSpaceNewResource() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),4);

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
    void addResourceNotEnoughSpaceOldResource() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),4);

        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

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
    void addResourceWrongResourceType() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),4);

        try {
            stash.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        Exception ex = assertThrows(WrongResourceTypeException.class, () -> {
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
    void addResourceBlockedResourceType() {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 1);
        ResourceDepot stash2 = new BasicDepot(warehouse, 2);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);

        try {
            stash1.addResource(ResourceType.SHIELD, 1);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        Exception ex = assertThrows(BlockedResourceException.class, () -> {
            stash2.addResource(ResourceType.SHIELD, 2);
        });

        String expectedMessage = "Error: Resource can't be added to depot because it is blocked by a different one.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the canHoldContentOf method in the affirmative case
     */
    @Test
    void canHoldContentOfTrue () {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 3);
        ResourceDepot stash2 = new BasicDepot(warehouse, 3);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);

        try {
            stash2.addResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertTrue (stash1.canHoldContentOf(stash2));

        try {
            stash1.addResource(ResourceType.COIN, 3);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertTrue (stash1.canHoldContentOf(stash2));
    }

    /**
     * This method tests the canHoldContentOf method in the negative case for size constraints
     */
    @Test
    void canHoldContentOfFalseSize () {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 1);
        ResourceDepot stash2 = new BasicDepot(warehouse, 2);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);

        try {
            stash2.addResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertFalse (stash1.canHoldContentOf(stash2));
    }

    /**
     * This method tests the canHoldContentOf method in the negative case for blocking constraints
     */
    @Test
    void canHoldContentOfFalseBlocking () {
        Warehouse warehouse = new Warehouse(0);
        ResourceDepot stash1 = new BasicDepot(warehouse, 3);
        ResourceDepot stash2 = new LeaderDepot(3, ResourceType.SHIELD);
        ResourceDepot stash3 = new BasicDepot(warehouse, 3);
        warehouse.addNewDepot(stash1);
        warehouse.addNewDepot(stash2);
        warehouse.addNewDepot(stash3);

        try {
            stash3.addResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        try {
            stash2.addResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertFalse (stash1.canHoldContentOf(stash2));
    }

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
     * This method tests the removal of an amount of resource greater than that present in stash
     */
    @Test
    void removeResourceNotEnough() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);

        try {
            stash.addResource(ResourceType.SHIELD, 3);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

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
    void removeResourceNotInStash() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),3);

        try {
            stash.addResource(ResourceType.COIN, 3);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

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
    void getNumOfResource() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),25);

        try {
            stash.addResource(ResourceType.SHIELD, 17);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        try {
            stash.removeResource(ResourceType.SHIELD, 16);
        } catch (NotEnoughResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        try {
            stash.addResource(ResourceType.SHIELD, 22);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertEquals(23, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the reaction of the getNumOfResource method to a previously present resource being deleted from the stash
     */
    @Test
    void getNumOfResourceRemoved() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),25);

        try {
            stash.addResource(ResourceType.SHIELD, 17);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        try {
            stash.removeResource(ResourceType.SHIELD, 17);
        } catch (NotEnoughResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
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
     * This method test the reaction of the getStoredResources method to resources being added and removed from stash
     */
    @Test
    void getStoredResources() {
        ResourceDepot stash = new BasicDepot(new Warehouse(0),5);

        try {
            stash.addResource(ResourceType.SHIELD, 4);
        } catch (NotEnoughSpaceException | WrongResourceTypeException | BlockedResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        List<ResourceType> output = stash.getStoredResources();
        assertTrue(output.contains(ResourceType.SHIELD));
        assertFalse(output.contains(ResourceType.COIN));

        try {
            stash.removeResource(ResourceType.SHIELD, 4);
        } catch (NotEnoughResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        output = stash.getStoredResources();
        assertFalse(output.contains(ResourceType.SHIELD));
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