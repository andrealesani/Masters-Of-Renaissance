package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.Exceptions.NotEnoughResourceException;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.List;


/**
 * This interface indicates that the implementing class can act as a generic container for resources as part of a player board.
 * It has methods for increasing and decreasing the contained resources, as well as visualizing its content.
 */
public interface ResourceStash {

    //PUBLIC METHODS

    /**
     * Remove a certain amount of the given resource from storage
     *
     * @param resource the resource to be decreased in quantity
     * @param quantity the amount of resource to remove from the amount stored
     * @throws NotEnoughResourceException - if the given resource is not present in the storage in the amount to be deleted
     */
    void removeResource(ResourceType resource, int quantity) throws NotEnoughResourceException;

    /**
     * Empties the storage of its entire content
     */
    void clear();

    //GETTERS

    /**
     * Returns the stored amount of the given resource
     *
     * @param resource - the resource the amount of which is asked
     * @return the amount of the given resource contained in storage
     */
    int getNumOfResource(ResourceType resource);

    /**
     * Returns the resources stored in storage
     *
     * @return a List of the stored resource types (if there are no resources in storage, the list is empty)
     */
    List<ResourceType> getStoredResources();
}
