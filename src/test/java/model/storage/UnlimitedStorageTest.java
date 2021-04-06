package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.ResourceNotPresentException;
import model.ResourceType;
import model.resource.ResourceShield;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnlimitedStorageTest {

    /**
     * This method tests the addition of resources to the storage
     */
    @Test
    void addResource() {
        UnlimitedStorage stash = new UnlimitedStorage();
        stash.addResource(ResourceType.SHIELD, 3);
        stash.addResource(ResourceType.SHIELD, 3);
        assertEquals(6, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of resources from the storage
     */
    @Test
    void removeResource() {
        UnlimitedStorage stash = new UnlimitedStorage();
        stash.addResource(ResourceType.SHIELD, 3);
        try {
            stash.removeResource(ResourceType.SHIELD, 2);
        } catch (NotEnoughResourceException | ResourceNotPresentException ex) {
            System.out.println(ex.getMessage());
            fail();
        }

        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the reaction of the getNumOfResource method to an empty storage
     */
    @Test
    void getNumOfResourceEmpty() {
        UnlimitedStorage stash = new UnlimitedStorage();
        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the reaction of the getStoredResources method to an empty storage
     */
    @Test
    void getStoredResourcesEmpty() {
        UnlimitedStorage stash = new UnlimitedStorage();
        assertTrue(stash.getStoredResources().isEmpty());
    }
}