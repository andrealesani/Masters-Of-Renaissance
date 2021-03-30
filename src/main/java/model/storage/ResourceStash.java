package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.ResourceNotPresentException;
import model.resource.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a generic container for resources as part of a player board.
 * It has methods for increasing and decreasing the contained resources, as well as visualizing its content.
 */
public abstract class ResourceStash {
    /**
     * Data structure used to map each stored resource with the amount stored
     */
    private final Map<Resource, Integer> storageContent = new HashMap<>();

    /**
     * Adds the given resource to storageContent
     * @param resource - the resource to be added
     * @param quantity - the amount of resource to add to the amount stored
     */
    public abstract void addResource (Resource resource, int quantity);

    /**
     Remove a certain amount of the given resource from storageContent
     * @param resource - the resource to be decreased in quantity
     * @param quantity - the amount of resource to remove from the amount stored
     * @throws ResourceNotPresentException - if the given resource is not present in storageContent
     * @throws NotEnoughResourceException - if the given resource is present in storageContent in fewer quantity than the amount to be deleted
     */
    public void removeResource (Resource resource, int quantity) throws ResourceNotPresentException, NotEnoughResourceException {
        if (storageContent.containsKey(resource) == false) {
            throw new ResourceNotPresentException();
        }
        int newQuantity = storageContent.get(resource) - quantity;

        if (newQuantity < 0) {
            throw new NotEnoughResourceException();
        } else if (newQuantity == 0) {
            storageContent.remove(resource);
        } else {
            storageContent.put(resource, newQuantity);
        }
    }
    /**
     * Returns the stored amount of the given resource
     * @param resource - the resource the amount of which is asked
     * @return The amount of the given resource contained in storageContent
     * @throws ResourceNotPresentException - if the given resource is not present in storageContent
     */
    public int getNumOfResource (Resource resource) throws ResourceNotPresentException {
        if (storageContent.containsKey(resource) == false) {
            throw new ResourceNotPresentException();
        }
        return storageContent.get(resource);
    }

    /**
     * Returns the resources stored in storageContent
     * @return A List of the stored resources
     */
    public List<Resource> getStoredResources () {
        List<Resource> resources = new ArrayList<>();
        resources.addAll(storageContent.keySet());
        return resources;
    }
}
