package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.Exceptions.NotEnoughResourceException;
import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import it.polimi.ingsw.model.Observable;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a storage for resources with no size limits or checks on addition of resources
 */
public class UnlimitedStorage implements ResourceStash, Observable {
    /**
     * Data structure used to map each stored resource with the amount stored
     */
    private Map<ResourceType, Integer> storageContent = new HashMap<>();

    //CONSTRUCTOR

    /**
     * Constructor
     */
    public UnlimitedStorage() {
        notifyObservers();
    }


    //PUBLIC METHODS

    /**
     * Adds the given resource to the storage
     *
     * @param resource the resource to be added
     * @param quantity the amount of resource to add to the amount stored
     */
    private void addResource(ResourceType resource, int quantity) {
        if (quantity < 0 || resource == null || !resource.canBeStored())
            throw new ParametersNotValidException();

        if (quantity > 0) {
            if (!storageContent.containsKey(resource)) {
                storageContent.put(resource, quantity);
            } else {
                storageContent.put(resource, storageContent.get(resource) + quantity);
            }
        }

        notifyObservers();
    }

    /**
     * Adds the given resources to the storage
     *
     * @param resources the resources to be added
     */
    public void addResources(Map<ResourceType, Integer> resources) {
        for (Map.Entry<ResourceType, Integer> resource : resources.entrySet()) {
            if (resource == null || resource.getValue() < 0 || !resource.getKey().canBeStored()) {
                throw new ParametersNotValidException();
            }

            if (resource.getKey() != null && resource.getValue() > 0) {
                if (!storageContent.containsKey(resource.getKey())) {
                    storageContent.put(resource.getKey(), resource.getValue());
                } else {
                    storageContent.put(resource.getKey(), storageContent.get(resource.getKey()) + resource.getValue());
                }
            }
        }

        notifyObservers();
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
        if (quantity < 0 || resource == null || !resource.canBeStored()) {
            throw new ParametersNotValidException();
        }
        if (quantity > 0) {
            if (!storageContent.containsKey(resource)) {
                throw new NotEnoughResourceException(resource);
            }

            int newQuantity = storageContent.get(resource) - quantity;

            if (newQuantity < 0) {
                throw new NotEnoughResourceException(resource);
            } else if (newQuantity == 0) {
                storageContent.remove(resource);
            } else {
                storageContent.put(resource, newQuantity);
            }
        }

        notifyObservers();
    }

    /**
     * Empties the storage of its entire content
     */
    @Override
    public void clear() {
        storageContent = new HashMap<>();

        notifyObservers();
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

    // PERSISTENCE METHODS

    /**
     * Restores the contents of the storage
     *
     * @param types the type of resource stored in the storage
     * @param quantities the amount of each resource stored in the storage
     */
    public void restoreContent(ResourceType types[], int[] quantities) {
        for (int i = 0; i < types.length; i++) {
            addResource(types[i], quantities[i]);
        }
    }

    // OBSERVABLE ATTRIBUTES AND METHODS

    /**
     * List of observers that need to get updated when the object state changes
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * This method calls the update() on every object observing this object
     */
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    /**
     * Adds an observer to the list of this object's observers
     *
     * @param observer the Observer to be added
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
        notifyObservers();
    }

    /**
     * Returns the list of this object's observers
     *
     * @return a List of the Observers
     */
    public List<Observer> getObservers() {
        return observers;
    }
}
