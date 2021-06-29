package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.Exceptions.NotEnoughResourceException;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.storage.UnlimitedStorage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UnlimitedStorageTest {

    /**
     * This method tests the addition of resources to the storage
     */
    @Test
    void addResources() {
        UnlimitedStorage stash = new UnlimitedStorage();
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.COIN, 8);
        resources.put(ResourceType.SERVANT, 15);
        resources.put(ResourceType.STONE, 16);
        resources.put(ResourceType.SHIELD, 42);
        stash.addResources(resources);

        assertEquals(42, stash.getNumOfResource(ResourceType.SHIELD));
        assertEquals(8, stash.getNumOfResource(ResourceType.COIN));
        assertEquals(15, stash.getNumOfResource(ResourceType.SERVANT));
        assertEquals(16, stash.getNumOfResource(ResourceType.STONE));

        resources.clear();
        resources.put(ResourceType.SHIELD, 27);
        stash.addResources(resources);
        assertEquals(69, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of resources from the storage
     */
    @Test
    void removeResource() throws NotEnoughResourceException {
        UnlimitedStorage stash = new UnlimitedStorage();
        stash.addResources(Map.of(ResourceType.SHIELD, 3));

        stash.removeResource(ResourceType.SHIELD, 2);

        assertEquals(1, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the removal of an amount of resource greater than that present in stash
     */
    @Test
    void removeResourceNotEnough() {
        UnlimitedStorage stash = new UnlimitedStorage();
        stash.addResources(Map.of(ResourceType.SHIELD, 3));

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });
    }

    /**
     * This method tests the removal of a resource that is not present in the stash
     */
    @Test
    void removeResourceNotInStash() {
        UnlimitedStorage stash = new UnlimitedStorage();
        stash.addResources(Map.of(ResourceType.COIN, 3));

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            stash.removeResource(ResourceType.SHIELD, 4);
        });
    }

    /**
     * This method tests the reaction of the getNumOfResource method to several operations being made on the fetched resource
     */
    @Test
    void getNumOfResource() throws NotEnoughResourceException {
        UnlimitedStorage stash = new UnlimitedStorage();

        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
        assertEquals(0, stash.getNumOfResource(ResourceType.COIN));

        stash.addResources(Map.of(ResourceType.SHIELD, 17));

        stash.removeResource(ResourceType.SHIELD, 16);

        stash.addResources(Map.of(ResourceType.SHIELD, 22));

        assertEquals(23, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method tests the reaction of the getNumOfResource method to a previously present resource being deleted from the stash
     */
    @Test
    void getNumOfResourceRemoved() throws NotEnoughResourceException {
        UnlimitedStorage stash = new UnlimitedStorage();

        stash.addResources(Map.of(ResourceType.SHIELD, 17));

        stash.removeResource(ResourceType.SHIELD, 17);

        assertEquals(0, stash.getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * This method test the reaction of the getStoredResources method to resources being added and removed from stash
     */
    @Test
    void getStoredResources() throws NotEnoughResourceException {
        UnlimitedStorage stash = new UnlimitedStorage();

        assertTrue(stash.getStoredResources().isEmpty());

        stash.addResources(Map.of(ResourceType.SHIELD, 4));
        stash.addResources(Map.of(ResourceType.COIN, 8));
        stash.addResources(Map.of(ResourceType.SERVANT, 15));
        stash.addResources(Map.of(ResourceType.SHIELD, 42));

        stash.removeResource(ResourceType.COIN, 8);
        stash.removeResource(ResourceType.SERVANT, 12);

        List<ResourceType> output = stash.getStoredResources();

        assertTrue(output.contains(ResourceType.SHIELD));
        assertFalse(output.contains(ResourceType.COIN));
        assertTrue(output.contains(ResourceType.SERVANT));
        assertFalse(output.contains(ResourceType.STONE));
    }
}