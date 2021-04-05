package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.ResourceNotPresentException;
import model.ResourceType;
import model.resource.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnlimitedStorage implements ResourceStash{
    /**
     * Data structure used to map each stored resource with the amount stored
     */
    private final Map<ResourceType, Integer> storageContent = new HashMap<>();

    /**
     * Adds the given resource to the storage
     * @param resource - the resource to be added
     * @param quantity - the amount of resource to add to the amount stored
     */
    public void addResource (ResourceType resource, int quantity){
        if (!storageContent.containsKey(resource)) {
            storageContent.put(resource, quantity);
        } else {
            storageContent.put(resource, storageContent.get(resource) + quantity);
        }
    }

    /**
     Remove a certain amount of the given resource from storage
     * @param resource - the resource to be decreased in quantity
     * @param quantity - the amount of resource to remove from the amount stored
     * @throws ResourceNotPresentException - if the given resource is not present in storage
     * @throws NotEnoughResourceException - if the given resource is present in storage in fewer quantity than the amount to be deleted
     */
    public void removeResource (ResourceType resource, int quantity) throws ResourceNotPresentException, NotEnoughResourceException {
        if (!storageContent.containsKey(resource)) {
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
     * @return the amount of the given resource contained in storage
     */
    public int getNumOfResource (ResourceType resource) {
        if (!storageContent.containsKey(resource)) {
            return 0;
        }
        return storageContent.get(resource);
    }

    /**
     * Returns the resources stored in storage
     * @return a List of the stored resources
     */
    public List<ResourceType> getStoredResources () {
        return new ArrayList<ResourceType>(storageContent.keySet());
    }
}
