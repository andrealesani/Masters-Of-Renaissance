package model.storage;

import model.resource.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a generic container for resources as part of a player board.
 * It has methods for increasing and decreasing the contained resources, as well as visualizing its content.
 */
public abstract class ResourceStash {
    private Map<Resource, Integer> storageContent = new HashMap<>();

    /**
     * Adds the given resource to storageContent
     */
    public abstract void addResource (Resource resource);

    /**
     * Remove a certain amount of the given resource from storageContent
     * @param resource - the resource to be decreased in quantity
     * @param quantity - the quantity of resource to remove from the amount stored
     */
    public void removeResource (Resource resource, int quantity) {
        //TODO
    }

    /**
     * Returns the stored amount of the given resource
     * @param resource - the resource the amount of which is asked
     * @return The amount of the given resource contained in storageContent
     */
    public int getNumOfResource (Resource resource) {
        //TODO
        return 0;
    }

    /**
     * Returns the resources whose store quantity is greater than zero
     * @return A List of the stored resources
     */
    public List<Resource> getStoredResources () {
        //TODO
        return null;
    }
}
