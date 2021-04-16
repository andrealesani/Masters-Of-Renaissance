package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.ParametersNotValidException;
import model.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a storage for resources with no size limits or checks on addition of resources
 */
public class UnlimitedStorage implements ResourceStash {
    /**
     * Data structure used to map each stored resource with the amount stored
     */
    private Map<ResourceType, Integer> storageContent = new HashMap<>();

    //PUBLIC METHODS

    /**
     * Adds the given resource to the storage
     *
     * @param resource the resource to be added
     * @param quantity the amount of resource to add to the amount stored
     */
    public void addResource(ResourceType resource, int quantity) {
        if (quantity < 0) {
            throw new ParametersNotValidException();
        }
        if (resource != null && quantity > 0) {
            if (!storageContent.containsKey(resource)) {
                storageContent.put(resource, quantity);
            } else {
                storageContent.put(resource, storageContent.get(resource) + quantity);
            }
        }
    }

    /**
     * Remove a certain amount of the given resource from storage
     *
     * @param resource the resource to be decreased in quantity
     * @param quantity the amount of resource to remove from the amount stored
     * @throws NotEnoughResourceException - if the given resource is not present in the storage in the amount to be deleted
     */
    @Override
    public void removeResource(ResourceType resource, int quantity) throws NotEnoughResourceException {
        if (quantity < 0) {
            throw new ParametersNotValidException();
        }
        if (resource != null && quantity > 0) {
            if (!storageContent.containsKey(resource)) {
                throw new NotEnoughResourceException();
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
    }

    /**
     * Empties the storage of its entire content
     */
    @Override
    public void clear() {
        storageContent = new HashMap<>();
    }

    //GETTERS

    /**
     * Returns the stored amount of the given resource
     *
     * @param resource the resource the amount of which is asked
     * @return the amount of the given resource contained in storage
     */
    @Override
    public int getNumOfResource(ResourceType resource) {
        if (storageContent.containsKey(resource)) {
            return storageContent.get(resource);
        }
        return 0;
    }

    /**
     * Returns the resources stored in storage
     *
     * @return a List of the stored resources
     */
    @Override
    public List<ResourceType> getStoredResources() {
        return new ArrayList<>(storageContent.keySet());
    }
}
