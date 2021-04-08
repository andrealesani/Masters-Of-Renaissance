package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.ResourceNotPresentException;
import model.ResourceType;
import java.util.List;


/**
 * This interface represents a generic container for resources as part of a player board.
 * It has methods for increasing and decreasing the contained resources, as well as visualizing its content.
 */
public interface ResourceStash {
    /**
     Remove a certain amount of the given resource from storage
     * @param resource - the resource to be decreased in quantity
     * @param quantity - the amount of resource to remove from the amount stored
     * @throws ResourceNotPresentException - if the given resource is not present in storage
     * @throws NotEnoughResourceException - if the given resource is present in storage in fewer quantity than the amount to be deleted
     */
    void removeResource (ResourceType resource, int quantity) throws ResourceNotPresentException, NotEnoughResourceException;

    /**
     * Returns the stored amount of the given resource
     * @param resource - the resource the amount of which is asked
     * @return the amount of the given resource contained in storage
     */
    int getNumOfResource (ResourceType resource);

    /**
     * Returns the resources stored in storage
     * @return a List of the stored resource types (if there are no resources in storage, the list is empty)
     */
    List<ResourceType> getStoredResources ();
}
